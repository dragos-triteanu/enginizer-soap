package com.enginizer.services.sas.client.converter.response;

import com.enginizer.services.sas.client.spi.LogoutResponseDTO;
import com.enginizer.services.sas.v1.LogoutResponse;

/**
 * Converter.
 */
public class LogoutResponseConverter implements ResponseConverter<LogoutResponse,LogoutResponseDTO> {

    /**
     * Converts a {@link LogoutResponseDTO} to a {@link LogoutResponse}.
     * @param logoutResponse {@link LogoutResponseDTO}.
     * @return {@link LogoutResponse}.
     */
    @Override
    public LogoutResponseDTO from(LogoutResponse logoutResponse) {
        LogoutResponseDTO dto = new LogoutResponseDTO();
        dto.setReasonCode(logoutResponse.getReasonCode());
        dto.setReturnCode(logoutResponse.getReturnCode());
        dto.setErrorCounter(logoutResponse.getErrorCounter());
        dto.setErrorMessage(logoutResponse.getErrorMessage());
        return dto;
    }
}
