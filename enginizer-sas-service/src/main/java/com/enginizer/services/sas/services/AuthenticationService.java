package com.enginizer.services.sas.services;

import com.enginizer.services.sas.model.ReturnCode;
import com.enginizer.services.sas.model.entities.*;
import com.enginizer.services.sas.model.entities.Principal;
import com.enginizer.services.sas.model.enums.CredentialStatus;
import com.enginizer.services.sas.model.enums.UserStatus;
import com.enginizer.services.sas.repository.FrontendRepository;
import com.enginizer.services.sas.repository.PrincipalRepository;
import com.enginizer.services.sas.repository.UserRepository;
import com.enginizer.services.sas.util.LegitimationDataEncoder;
import com.enginizer.services.sas.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

/**
 * Service which deals with the authentication of the user. Interacts with various repository in order to
 * perform authentication.
 */
@Service
public class AuthenticationService {

    @Value("${sas.authentication.login.attempts.counter}")
    protected Integer loginAttemptsCounter;

    @Value("${sas.authentication.login.attempts.timer}")
    protected Integer loginAttemptsTimer;

    private PrincipalRepository principalRepository;
    private UserRepository userRepository;
    private FrontendRepository frontendRepository;
    private SessionService sessionService;

    @Autowired
    public AuthenticationService(final PrincipalRepository principalRepository, final UserRepository userRepository,
                                 final FrontendRepository frontendRepository, final SessionService sessionService) {
        this.principalRepository = principalRepository;
        this.userRepository = userRepository;
        this.frontendRepository = frontendRepository;
        this.sessionService = sessionService;
    }

    /**
     * Performs the user's authentication by verifying if it has the required role and that the provided
     * credentials match the ones associated to the user.
     *
     * @param loginRequest contains the authentication data provided by the user
     * @return the response for the authentication
     */
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {

        // retrieve principal
        Principal principal = retrievePrincipal(loginRequest.getPrincipal().getPrincipalData(),
                loginRequest.getPrincipal().getPrincipalType());
        if (principal == null) {
            return createLoginResponse(ReturnCode.DENIED_INVALID_PRINCIPALS);
        }

        // retrieve user data
        User userData = userRepository.findOne(principal.getUserID());
        if (userData == null) {
            return createLoginResponse(ReturnCode.DENIED_INVALID_USERID);
        }

        // check user's status
        if (!UserStatus.ACTIVE.getValue().equals(userData.getStatus())) {
            return createLoginResponse(ReturnCode.DENIED_USER_IS_LOCKED);
        }

        // retrieve frontend
        Frontend frontend = frontendRepository.findOne(loginRequest.getFrontendID());
        if (frontend == null) {
            return createLoginResponse(ReturnCode.DENIED_INVALID_FRONTEND_ID);
        }

        // check if the credential type is valid for the frontend
        if (!isValidCredentialForFrontend(loginRequest.getLegitimation().getType(), frontend.getFrontendID())) {
            return createLoginResponse(ReturnCode.DENIED_INVALID_CREDENTIAL_FOR_FRONTEND);
        }

        // check user's credential
        Credential matchedCredential = null;
        for (Credential credential : principal.getCredentials()) {
            if (loginRequest.getLegitimation().getType().equals(credential.getCredentialTypeID())) {
                matchedCredential = credential;

                if (LegitimationDataEncoder.matches(loginRequest.getLegitimation().getLegitimationData(), credential.getCredentialData())) {
                    // check credential status
                    if (CredentialStatus.MUST_CHANGE.getValue().equals(credential.getStatus())) {
                        return createLoginResponse(ReturnCode.DENIED_MUST_CHANGE_PASSWORD);
                    }
                    if (CredentialStatus.EXPIRED.getValue().equals(credential.getStatus())) {
                        return createLoginResponse(ReturnCode.DENIED_CREDENTIAL_EXPIRED);
                    }

                    // successful authentication: update user's last login date and reset login failures
                    userRepository.updateLastLoginDateAndResetLoginFailures(userData.getUserID(),
                            new java.sql.Date(new Date().getTime()), 0, null);

                    // prepare successful login response
                    String sessionToken = sessionService.generateAndSaveSession(userData.getUserID(), principal,
                            loginRequest.getFrontendID());

                    LoginResponse loginResponse = createLoginResponse(ReturnCode.ACCEPTED);
                    loginResponse.setSessionToken(sessionToken);
                    loginResponse.setRoles(convertRoles(userData.getRoles()));
                    loginResponse.setReturnCode(ReturnCode.ACCEPTED.getCode());
                    loginResponse.setSessionTimeout((int) sessionService.getSessionTimeout());
                    return loginResponse;
                }
            }
        }

        if (matchedCredential == null) {
            return createLoginResponse(ReturnCode.DENIED_INVALID_CREDENTIALS);
        } else {
            // credential data does not match: verify login failures
            int failuresCounter = computeAndUpdateLoginFailures(userData);

            if (loginAttemptsCounter == failuresCounter) {

                // maximum login failures reached, lock account
                userRepository.updateUserStatus(userData.getUserID(), UserStatus.PASSWORD_LOCKED.getValue());
                return createLoginResponseWithFailCounter(ReturnCode.DENIED_USER_IS_LOCKED_TOO_MANY_WRONG_PASSWORDS, failuresCounter);
            }

            LoginResponse loginResponse = createLoginResponseWithFailCounter(ReturnCode.DENIED_INVALID_PASSWORD, failuresCounter);
            loginResponse.setAttemptsLeft(loginAttemptsCounter - failuresCounter);
            return loginResponse;
        }
    }


    /**
     * Service method for executing the logout operation.
     *
     * @param request see {@link LogoutRequest}, for destroying a user session.
     * @return {@link LogoutResponse}.
     */
    @Transactional
    public LogoutResponse logout(final LogoutRequest request) {
        Reply reply = sessionService.destroySession(request.getSessionToken());
        return buildLogoutResponse(reply);
    }

    private LogoutResponse buildLogoutResponse(Reply reply) {
        LogoutResponse response = new LogoutResponse();
        response.setErrorMessage(reply.getErrorMessage());
        response.setErrorCounter(reply.getErrorCounter());
        response.setReturnCode(reply.getReturnCode());
        response.setReasonCode(reply.getReasonCode());
        return response;
    }


    private Principal retrievePrincipal(final String principalID, final String principalType) {
        return principalRepository.findByPrincipalDataAndPrincipalTypeID(principalID, principalType);
    }

    private boolean isValidCredentialForFrontend(final String type, final String frontendID) {

        Frontend frontend = frontendRepository.findOne(frontendID);
        Optional<CredentialType> first = frontend.getCredentialTypes().stream().filter(credentialType -> credentialType.getCredentialTypeID().equalsIgnoreCase(type)).findFirst();

        return first.isPresent();
    }

    /**
     * Computes the user's failed login attempts by verifying if the configured duration since the last failed login
     * is still active. If it's not active the failed login counter is resetted. The computed value is incremented
     * and updated in the DB.
     *
     * @param dbUser user entity
     * @return number of failed login tries
     */
    private int computeAndUpdateLoginFailures(User dbUser) {
        int failuresCounter = dbUser.getLoginFailures() != null ? dbUser.getLoginFailures() : 0;
        Date failureDate = dbUser.getLoginFailureDate();

        if (failureDate != null) {
            // check if login failure timer has timed out
            long loginFailureTime = failureDate.getTime();
            long now = new Date().getTime();

            boolean isLoginFailureActive = now < loginFailureTime + loginAttemptsTimer;

            failuresCounter = isLoginFailureActive ? failuresCounter : 0;
        }

        userRepository.updateLoginFailuresAndDate(dbUser.getUserID(), ++failuresCounter,
                failureDate != null ? new java.sql.Date(failureDate.getTime()) : new java.sql.Date(new Date().getTime()));

        return failuresCounter;
    }

    /**
     * Creates a login response containing the return code which indicates if the authentication succeeded or
     * not. In case it was unsuccessful it provides a short description for the reason.
     *
     * @param returnCode the code and description set on the response
     * @return a response for the login
     */
    private LoginResponse createLoginResponse(ReturnCode returnCode) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setReturnCode(returnCode.getCode());

        if (!ReturnCode.ACCEPTED.equals(returnCode))
            loginResponse.setErrorMessage(returnCode.getDescription());

        return loginResponse;
    }

    /**
     * Creates a {@link LoginResponse} with the errorCounter field populated
     *
     * @param returnCode the return code.
     * @param failCount  the number of failures for the login operation.
     * @return LoginResponse.
     */
    private LoginResponse createLoginResponseWithFailCounter(ReturnCode returnCode, final int failCount) {
        LoginResponse response = createLoginResponse(returnCode);
        response.setErrorCounter(failCount);
        return response;
    }

    private Roles convertRoles(Set<Role> roleList) {
        Roles roles = new Roles();

        for (Role role : roleList) {
            UserRole userRole = new UserRole();
            userRole.setId(role.getRoleID());
            roles.getRole().add(userRole);
        }
        return roles;
    }

}
