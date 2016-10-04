package com.enginizer.services.sas.services;

import com.enginizer.services.sas.model.ReturnCode;
import com.enginizer.services.sas.model.entities.Principal;
import com.enginizer.services.sas.model.entities.Session;
import com.enginizer.services.sas.repository.SessionRepository;
import com.enginizer.services.sas.v1.Reply;
import com.enginizer.services.sas.v1.VerifySessionRequest;
import com.enginizer.services.sas.v1.VerifySessionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;


/**
 * Service that interacts with the session repository and deals with operations related to the session.
 */
@Service
public class SessionService {

    /**
     * Configurable duration for which a session is valid (in milliseconds).
     */
    @Value("${sas.authentication.session.timeout}")
    long sessionTimeout;

    private SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    /**
     * Generates a session token and saves it in the DB associating it with the user.
     *
     * @param userID the identifier of the user for which the session is created
     * @return a session token
     */
    @Transactional
    public String generateAndSaveSession(Long userID, Principal principals, String frontendID) {
        UUID sessionToken = UUID.randomUUID();

        String strippedSessionToken = sessionToken.toString().replaceAll("-", "");

        Session session = new Session();
        session.setSessionID(strippedSessionToken);

        Date now = new Date();

        session.setSessionCreated(now);
        session.setUserID(userID);
        session.setSessionLastUsed(now);
        session.setPrincipalID(principals.getPrincipalData());
        session.setPrincipalTypeID(principals.getPrincipalTypeID());
        session.setFrontendID(frontendID);
        // persist session
        sessionRepository.save(session);

        return strippedSessionToken;
    }


    /**
     * Method that destroys a user session by removing it from the database, and populates
     * the {@link Reply} based on the outcome.
     *
     * @param sessionToken the session identifier for the session that will be destroyed.
     */
    public Reply destroySession(final String sessionToken) {
        Reply reply = new Reply();
        try {
            sessionRepository.delete(sessionToken);
            reply.setReturnCode(ReturnCode.ACCEPTED.getCode());
        } catch (DataAccessException dae) {
            reply.setReturnCode(ReturnCode.INVALID_SESSIONID.getCode());
            reply.setErrorMessage(ReturnCode.INVALID_SESSIONID.getDescription());
        }
        return reply;
    }

    /**
     * Verifies a session's validity by checking if it was not closed and that the duration from when it was last used
     * is not over the configured session timeout. If the provided session is valid it renews it if not it closes it.
     *
     * @param request containing the sessionToken to be verified
     * @return response containing a boolean indicating whether the session is valid or not
     */
    @Transactional
    public VerifySessionResponse verifySession(VerifySessionRequest request) {
        VerifySessionResponse response = new VerifySessionResponse();
        response.setIsValid(false);

        Session session = sessionRepository.findOne(request.getSessionToken());

        // check if the session exists and that it wasn't closed
        if(session != null && session.getSessionClosed() == null) {
            long sessionLastUsed = session.getSessionLastUsed().getTime();
            long now = new Date().getTime();

            // verify if the session should still be active
            boolean isActive = now < sessionLastUsed + sessionTimeout;

            if (isActive) {
                // renew session
                sessionRepository.updateSessionLastUsed(request.getSessionToken(), new java.sql.Date(now));
            } else {
                // close session
                sessionRepository.updateSessionClosed(request.getSessionToken(), new java.sql.Date(now));
            }

            response.setIsValid(isActive);
        }
        return response;
    }

    public long getSessionTimeout() {
        return sessionTimeout;
    }
}
