package com.enginizer.services.sas.client.handler;

import com.enginizer.services.sas.client.converter.request.LogoutRequestConverter;
import com.enginizer.services.sas.client.converter.response.LogoutResponseConverter;
import com.enginizer.services.sas.client.exception.SASException;
import com.enginizer.services.sas.client.spi.LogoutRequestDTO;
import com.enginizer.services.sas.client.spi.LogoutResponseDTO;
import com.enginizer.services.sas.v1.LogoutRequest;
import com.enginizer.services.sas.v1.LogoutResponse;
import org.springframework.ws.client.core.WebServiceTemplate;

/**
 * Handler for the SAS' logout operation.
 */
public class LogoutHandlerAdapter implements HandlerAdapter<LogoutRequestDTO,LogoutResponseDTO> {

    private WebServiceTemplate logoutServiceTemplate;
    private LogoutRequestConverter requestConverter;
    private LogoutResponseConverter responseConverter;

    public LogoutHandlerAdapter(WebServiceTemplate logoutServiceTemplate, LogoutRequestConverter requestConverter, LogoutResponseConverter responseConverter) {
        this.logoutServiceTemplate = logoutServiceTemplate;
        this.requestConverter = requestConverter;
        this.responseConverter = responseConverter;
    }

    @Override
    public LogoutResponseDTO adaptAndHandle(LogoutRequestDTO logoutRequestDTO) throws SASException {
        LogoutRequest request = requestConverter.from(logoutRequestDTO);
        LogoutResponse response = (LogoutResponse) logoutServiceTemplate.marshalSendAndReceive(request);
        return responseConverter.from(response);
    }
}
