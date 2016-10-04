package com.enginizer.services.sas.services;

import com.enginizer.services.sas.model.entities.Credential;
import com.enginizer.services.sas.model.entities.Principal;
import com.enginizer.services.sas.model.entities.User;
import com.enginizer.services.sas.model.enums.CredentialStatus;
import com.enginizer.services.sas.repository.CredentialRepository;
import com.enginizer.services.sas.repository.SessionRepository;
import com.enginizer.services.sas.repository.UserRepository;
import com.enginizer.services.sas.util.LegitimationDataEncoder;
import com.enginizer.services.sas.v1.Legitimation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Service class which manages MTAN related logic.
 */
@Service
public class MTANService {
    private SessionRepository sessionRepository;
    private UserRepository userRepository;
    private CredentialRepository credentialRepository;

    @Autowired
    public MTANService(final SessionRepository sessionRepository,
                       final UserRepository userRepository,
                       final CredentialRepository credentialRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.credentialRepository = credentialRepository;
    }


    /**
     * Verifies whether the specifier {@link User} has the MTAN {@link Legitimation}.
     *
     * @param user         {@link User}.
     * @param legitimation {@link Legitimation}.
     * @return true if user has the MTAN legitimation,false otherwise.
     */
    @Transactional
    public Boolean verifyMTANLegitimation(final User user, final Legitimation legitimation) {
        Boolean hasProperCredentials = false;
        //also authenticate using provided legitimation
        for (Principal userPrincipal : user.getPrincipals()) {
            for (Credential userCredential : userPrincipal.getCredentials()) {
                if (userCredential.getCredentialType().getCredentialTypeID().equals(legitimation.getType())) {
                    if (CredentialStatus.ACTIVE.getValue().equals(userCredential.getStatus())) {
                        if (LegitimationDataEncoder.matches(legitimation.getLegitimationData(), userCredential.getCredentialData())) {
                            if (null != userCredential.getValidUntil() && new Date().after(userCredential.getValidUntil())) {
                                hasProperCredentials = null;
                            } else {
                                hasProperCredentials = true;
                            }
                            userCredential.setStatus(CredentialStatus.EXPIRED.getValue());
                            credentialRepository.save(userCredential);
                            break;
                        }
                    }

                }
            }
        }
        return hasProperCredentials;
    }
}
