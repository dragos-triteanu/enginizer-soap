package com.enginizer.services.sas.client.converter.request;

import com.enginizer.services.sas.client.spi.LogoutRequestDTO;
import com.enginizer.services.sas.v1.LogoutRequest;

/**
 * Converter.
 */
public class LogoutRequestConverter implements RequestConverter<LogoutRequestDTO,LogoutRequest> {

    /**
     * Converts a {@link LogoutRequestDTO} into a {@link LogoutRequest}.
     * @param logoutRequestDTO {@link LogoutRequestDTO}.
     * @return {@link LogoutRequest}.
     */
    @Override
    public LogoutRequest from(LogoutRequestDTO logoutRequestDTO) {
        LogoutRequest request = new LogoutRequest();
        request.setSessionToken(logoutRequestDTO.getSessionToken());
        return request;
    }
}
