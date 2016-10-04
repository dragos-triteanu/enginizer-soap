package com.enginizer.services.sas.client.converter.request;

import com.enginizer.services.sas.client.spi.LoginRequestDTO;
import com.enginizer.services.sas.v1.Legitimation;
import com.enginizer.services.sas.v1.LoginRequest;
import com.enginizer.services.sas.v1.Principal;

/**
 * Converter.
 */
public class LoginRequestConverter implements RequestConverter<LoginRequestDTO,LoginRequest> {

    /**
     * Converts a {@link LoginRequestDTO} into a {@link LoginRequest}.
     * @param loginRequestDTO {@link LoginRequestDTO}.
     * @return {@link LoginRequest}.
     */
    @Override
    public LoginRequest from(LoginRequestDTO loginRequestDTO) {
        LoginRequest request = new LoginRequest();
        request.setFrontendID(loginRequestDTO.getFrontendID());

        if(null != loginRequestDTO.getLegitimation()){
            Legitimation legitimation = new Legitimation();
            legitimation.setType(loginRequestDTO.getLegitimation().getType());
            legitimation.setLegitimationData(loginRequestDTO.getLegitimation().getData());
            request.setLegitimation(legitimation);
        }

        if(null != loginRequestDTO.getUser()){
            Principal userId = new Principal();
            userId.setPrincipalType(loginRequestDTO.getUser().getPrincipalType());
            userId.setPrincipalData(loginRequestDTO.getUser().getId());
            request.setPrincipal(userId);
        }
        return request;
    }
}
