package com.enginizer.services.sas.client.converter.response;

import com.enginizer.services.sas.client.spi.VerifySessionResponseDTO;
import com.enginizer.services.sas.v1.VerifySessionResponse;

/**
 * Converter.
 */
public class VerifySessionResponseConverter implements ResponseConverter<VerifySessionResponse, VerifySessionResponseDTO> {

    /**
     * Converts a {@link VerifySessionResponse} into a {@link VerifySessionResponseDTO}.
     *
     * @param verifySessionResponse {@link VerifySessionResponse}.
     * @return {@link VerifySessionResponseDTO}.
     */
    @Override
    public VerifySessionResponseDTO from(VerifySessionResponse verifySessionResponse) {
        VerifySessionResponseDTO verifySessionResponseDTO = new VerifySessionResponseDTO(verifySessionResponse.isIsValid());
        return verifySessionResponseDTO;
    }
}
