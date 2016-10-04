package com.enginizer.services.sas.services;

import com.enginizer.services.sas.model.ReturnCode;
import com.enginizer.services.sas.model.entities.*;
import com.enginizer.services.sas.model.enums.CredentialStatus;
import com.enginizer.services.sas.model.enums.UserStatus;
import com.enginizer.services.sas.repository.FrontendRepository;
import com.enginizer.services.sas.repository.PrincipalRepository;
import com.enginizer.services.sas.repository.UserRepository;
import com.enginizer.services.sas.util.LegitimationDataEncoder;
import com.enginizer.services.sas.v1.Legitimation;
import com.enginizer.services.sas.v1.LoginRequest;
import com.enginizer.services.sas.v1.LoginResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AuthenticationTest {

    private static final Long USER_IDENTIFIER = 1234L;
    private static final String CUSTOMER_PRINCIPAL_TYPE = "CustomerNumber";
    private static final String USER_PASSWORD_CREDENTIAL_TYPE = "UserDefinedPassword";
    private static final String MTAN_CREDENTIAL_TYPE = "MTAN";
    private static final String CREDENTIAL_DATA = "Password";
    private static final String OTHER_CREDENTIAL_DATA = "drowssaP";
    private static final String OWNER_ROLE = "Owner";
    private static final String READ_ONLY_ROLE = "ReadOnly";
    private static final String FRONTEND_ID = "Frontend";
    private static final String USER_ID = "UserID";
    private static final String SESSION_TOKEN = "SessionToken";

    @Mock
    private PrincipalRepository principalRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FrontendRepository frontendRepository;
    @Mock
    private SessionService sessionService;

    @InjectMocks
    private AuthenticationService victim;

    private Frontend frontend;

    @Before
    public void setup() {
        initMocks(this);

        ReflectionTestUtils.setField(victim,"loginAttemptsCounter", 5);
        ReflectionTestUtils.setField(victim,"loginAttemptsTimer", 1800000);

        when(frontendRepository.findOne(eq(FRONTEND_ID))).thenReturn(buildMockFrontend());
    }

    private Frontend buildMockFrontend() {
        frontend = new Frontend();
        frontend.setFrontendID(FRONTEND_ID);
        CredentialType type = new CredentialType();
        type.setCredentialTypeID(USER_PASSWORD_CREDENTIAL_TYPE);
        frontend.setCredentialTypes(new HashSet<>(asList(type)));
        return  frontend;
    }

    /**
     * Tests a login attempt with a request containing an invalid user principalID.
     */
    @Test
    public void shouldFailLoginInvalidPrincipalID() {
        when(principalRepository.findByPrincipalDataAndPrincipalTypeID(anyString(),anyString())).thenReturn(null);

        LoginResponse response = victim.login(getLoginRequest(CUSTOMER_PRINCIPAL_TYPE, USER_PASSWORD_CREDENTIAL_TYPE,
                CREDENTIAL_DATA));

        assertEquals(ReturnCode.DENIED_INVALID_PRINCIPALS.getCode(), response.getReturnCode());
        assertEquals(ReturnCode.DENIED_INVALID_PRINCIPALS.getDescription(), response.getErrorMessage());
    }

    /**
     * Tests a login attempt for a user which has its account status LOCKED.
     */
    @Test
    public void shouldFailLoginInvalidUserStatus() {
        when(principalRepository.findByPrincipalDataAndPrincipalTypeID(anyString(),anyString())).thenReturn(getPrincipal());
        when(userRepository.findOne(anyLong())).thenReturn(getUser(UserStatus.LOCKED.getValue(), OWNER_ROLE));

        LoginResponse response = victim.login(getLoginRequest(CUSTOMER_PRINCIPAL_TYPE, USER_PASSWORD_CREDENTIAL_TYPE,
                CREDENTIAL_DATA));

        assertEquals(ReturnCode.DENIED_USER_IS_LOCKED.getCode(), response.getReturnCode());
        assertEquals(ReturnCode.DENIED_USER_IS_LOCKED.getDescription(), response.getErrorMessage());
    }

    /**
     * Tests a login attempt with a request containing an invalid frontend identifier.
     */
    @Test
    public void shouldFailLoginInvalidFrontend() {
        when(principalRepository.findByPrincipalDataAndPrincipalTypeID(anyString(),anyString())).thenReturn(getPrincipal());
        when(userRepository.findOne(anyLong())).thenReturn(getUser(UserStatus.ACTIVE.getValue(), OWNER_ROLE));
        when(frontendRepository.findOne(anyString())).thenReturn(null);

        LoginResponse response = victim.login(getLoginRequest(CUSTOMER_PRINCIPAL_TYPE, USER_PASSWORD_CREDENTIAL_TYPE,
                CREDENTIAL_DATA));

        assertEquals(ReturnCode.DENIED_INVALID_FRONTEND_ID.getCode(), response.getReturnCode());
        assertEquals(ReturnCode.DENIED_INVALID_FRONTEND_ID.getDescription(), response.getErrorMessage());
    }

    /**
     * Tests a login attempt with a credential type that is not valid for the requested frontend.
     */
    @Test
    public void shouldFailLoginInvalidCredentialFrontend() {
        when(principalRepository.findByPrincipalDataAndPrincipalTypeID(anyString(),anyString())).thenReturn(getPrincipal());
        when(userRepository.findOne(anyLong())).thenReturn(getUser(UserStatus.ACTIVE.getValue(), READ_ONLY_ROLE));
        when(frontendRepository.findOne(anyString())).thenReturn(mock(Frontend.class));

        LoginResponse response = victim.login(getLoginRequest(CUSTOMER_PRINCIPAL_TYPE, USER_PASSWORD_CREDENTIAL_TYPE,
                CREDENTIAL_DATA));

        assertEquals(ReturnCode.DENIED_INVALID_CREDENTIAL_FOR_FRONTEND.getCode(), response.getReturnCode());
        assertEquals(ReturnCode.DENIED_INVALID_CREDENTIAL_FOR_FRONTEND.getDescription(), response.getErrorMessage());
    }

    /**
     * Tests a login attempt with credentials that doesn't have the required credential type.
     */
    @Test
    public void shouldFailLoginInvalidCredentialType() {
        when(principalRepository.findByPrincipalDataAndPrincipalTypeID(anyString(),anyString())).thenReturn(
                getPrincipal(getCredential(USER_PASSWORD_CREDENTIAL_TYPE, CREDENTIAL_DATA, CredentialStatus.ACTIVE.getValue())));
        when(userRepository.findOne(anyLong())).thenReturn(getUser(UserStatus.ACTIVE.getValue(), OWNER_ROLE));

        LoginResponse response = victim.login(getLoginRequest(CUSTOMER_PRINCIPAL_TYPE, MTAN_CREDENTIAL_TYPE,
                CREDENTIAL_DATA));

        assertEquals(ReturnCode.DENIED_INVALID_CREDENTIAL_FOR_FRONTEND.getCode(), response.getReturnCode());
        assertEquals(ReturnCode.DENIED_INVALID_CREDENTIAL_FOR_FRONTEND.getDescription(), response.getErrorMessage());
    }

    /**
     * Tests a first login attempt with credential data that doesn't match.
     */
    @Test
    public void shouldFailLoginInvalidCredentialDataFirstAttempt() {
        when(principalRepository.findByPrincipalDataAndPrincipalTypeID(anyString(),anyString())).thenReturn(
                getPrincipal(getCredential(USER_PASSWORD_CREDENTIAL_TYPE, CREDENTIAL_DATA, CredentialStatus.ACTIVE.getValue())));
        when(userRepository.findOne(anyLong())).thenReturn(getUser(UserStatus.ACTIVE.getValue(), OWNER_ROLE));

        // TODO Add argumentCaptor for userRepository.updateLoginFailuresAndDate()
        LoginResponse response = victim.login(getLoginRequest(CUSTOMER_PRINCIPAL_TYPE, USER_PASSWORD_CREDENTIAL_TYPE,
                OTHER_CREDENTIAL_DATA));

        assertEquals(ReturnCode.DENIED_INVALID_PASSWORD.getCode(), response.getReturnCode());
        assertEquals(ReturnCode.DENIED_INVALID_PASSWORD.getDescription(), response.getErrorMessage());
    }

    /**
     * Tests the last permitted login attempt with credential data that doesn't match.
     * After this attempt the user's account is blocked.
     */
    @Test
    public void shouldFailLoginInvalidCredentialDataLockAccount() {
        when(principalRepository.findByPrincipalDataAndPrincipalTypeID(anyString(),anyString())).thenReturn(
                getPrincipal(getCredential(USER_PASSWORD_CREDENTIAL_TYPE, CREDENTIAL_DATA, CredentialStatus.ACTIVE.getValue())));

        // set loginFailures to the last permitted attempt
        User userData = getUser(UserStatus.ACTIVE.getValue(), OWNER_ROLE);
        userData.setLoginFailures(victim.loginAttemptsCounter - 1);

        when(userRepository.findOne(anyLong())).thenReturn(userData);

        // TODO Add argumentCaptor for userRepository.updateUserStatus()
        LoginResponse response = victim.login(getLoginRequest(CUSTOMER_PRINCIPAL_TYPE, USER_PASSWORD_CREDENTIAL_TYPE,
                OTHER_CREDENTIAL_DATA));

        assertEquals(ReturnCode.DENIED_USER_IS_LOCKED_TOO_MANY_WRONG_PASSWORDS.getCode(), response.getReturnCode());
        assertEquals(ReturnCode.DENIED_USER_IS_LOCKED_TOO_MANY_WRONG_PASSWORDS.getDescription(), response.getErrorMessage());
    }

    /**
     * Tests a login attempt for a user that is on its last permitted login attempt but the duration for the login
     * failures has timed out so the counter gets resetted.
     */
    @Test
    public void shouldFailLoginInvalidCredentialDataResetFailures() {
        when(principalRepository.findByPrincipalDataAndPrincipalTypeID(anyString(),anyString())).thenReturn(
                getPrincipal(getCredential(USER_PASSWORD_CREDENTIAL_TYPE, CREDENTIAL_DATA, CredentialStatus.ACTIVE.getValue())));

        // set loginFailures to the last permitted attempt
        User userData = getUser(UserStatus.ACTIVE.getValue(), OWNER_ROLE);
        userData.setLoginFailures(victim.loginAttemptsCounter - 1);

        // set user's last login failure 2 minutes after the configured failures reset duration
        long twoMinutesInMilliseonds = 120000;
        userData.setLoginFailureDate(new Date(new Date().getTime() - (victim.loginAttemptsTimer + twoMinutesInMilliseonds)));

        when(userRepository.findOne(anyLong())).thenReturn(userData);

        LoginResponse response = victim.login(getLoginRequest(CUSTOMER_PRINCIPAL_TYPE, USER_PASSWORD_CREDENTIAL_TYPE,
                OTHER_CREDENTIAL_DATA));

        assertEquals(ReturnCode.DENIED_INVALID_PASSWORD.getCode(), response.getReturnCode());
        assertEquals(ReturnCode.DENIED_INVALID_PASSWORD.getDescription(), response.getErrorMessage());
    }

    /**
     * Tests a successful login flow with a valid user that has an ACTIVE status, the required role, principal
     * and credential with a matching credential data.
     */
    @Test
    public void shouldSuccessfullyLogin() {
        when(principalRepository.findByPrincipalDataAndPrincipalTypeID(anyString(),anyString())).thenReturn(
                getPrincipal(getCredential(USER_PASSWORD_CREDENTIAL_TYPE,
                        LegitimationDataEncoder.encryptLegitimationPassword(CREDENTIAL_DATA), CredentialStatus.ACTIVE.getValue())));
        when(userRepository.findOne(anyLong())).thenReturn(getUser(UserStatus.ACTIVE.getValue(), OWNER_ROLE));
        when(sessionService.generateAndSaveSession(any(Long.class), any(Principal.class),anyString())).thenReturn(SESSION_TOKEN);

        LoginResponse response = victim.login(getLoginRequest(CUSTOMER_PRINCIPAL_TYPE, USER_PASSWORD_CREDENTIAL_TYPE,
                CREDENTIAL_DATA));

        assertEquals(ReturnCode.ACCEPTED.getCode(), response.getReturnCode());
        assertEquals(null, response.getErrorMessage());
        assertEquals(SESSION_TOKEN, response.getSessionToken());
    }

    /**
     * Creates a Login request with the received data.
     *
     * @param principalType the principal type required for a successful login
     * @param credentialType the credential type required for a successful login
     * @param credentialData the credential data submitted by the user
     * @return login request populated with the above data
     */
    private LoginRequest getLoginRequest(String principalType, String credentialType, String credentialData) {
        com.enginizer.services.sas.v1.Principal userId = new com.enginizer.services.sas.v1.Principal();
        userId.setPrincipalData(USER_ID);
        userId.setPrincipalType(principalType);

        Legitimation legitimation = new Legitimation();
        legitimation.setLegitimationData(credentialData);
        legitimation.setType(credentialType);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setFrontendID(FRONTEND_ID);
        loginRequest.setPrincipal(userId);
        loginRequest.setLegitimation(legitimation);
        loginRequest.setFrontendID(FRONTEND_ID);
        return loginRequest;
    }

    private Principal getPrincipal(final Credential ...credentials) {
        Principal principal = new Principal();
        principal.setUserID(USER_IDENTIFIER);

        Set<Credential> credentialsSet = new HashSet<>();
        for (Credential credential : credentials) {
            credentialsSet.add(credential);
        }
        principal.setCredentials(credentialsSet);

        return principal;
    }

    private Credential getCredential(final String credentialType, final String credentialData, final String status) {
        Credential credential = new Credential();

        credential.setCredentialTypeID(credentialType);
        credential.setCredentialData(credentialData);
        credential.setStatus(status);

        return credential;
    }

    /**
     * Creates a user entity like one returned from the repository populated with the desired data.
     *
     * @param status the status for the user
     * @param roleName the role name the user own
     * @return user populated with the above data
     */
    private User getUser(String status, String roleName) {
        User user = new User();
        user.setUserID(USER_IDENTIFIER);
        user.setStatus(status);
        user.setLoginFailures(0);

        Set<Role> roles = new HashSet<>();
        roles.add(getRole(roleName));

        user.setRoles(roles);

        return user;
    }

    /**
     * Creates a role entity.
     *
     * @param roleName the role name
     * @return role entity with its name
     */
    private Role getRole(String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);

        return role;
    }
}
