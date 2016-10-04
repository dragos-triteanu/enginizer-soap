package com.enginizer.services.sas.client.converter.request;

import com.enginizer.services.sas.client.spi.VerifySessionRequestDTO;
import com.enginizer.services.sas.v1.VerifySessionRequest;

/**
 * Created by cs94749 on 27.04.2016.
 */
public class VerifySessionRequestConverter implements RequestConverter<VerifySessionRequestDTO, VerifySessionRequest> {

    @Override
    public VerifySessionRequest from(VerifySessionRequestDTO verifySessionRequestDTO) {
        VerifySessionRequest verifySessionRequest = new VerifySessionRequest();
        verifySessionRequest.setSessionToken(verifySessionRequestDTO.getSessionToken());
        return verifySessionRequest;
    }
}
