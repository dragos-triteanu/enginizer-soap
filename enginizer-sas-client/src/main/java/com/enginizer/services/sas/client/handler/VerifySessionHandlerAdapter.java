package com.enginizer.services.sas.client.handler;

import com.enginizer.services.sas.client.converter.request.VerifySessionRequestConverter;
import com.enginizer.services.sas.client.converter.response.VerifySessionResponseConverter;
import com.enginizer.services.sas.client.exception.SASException;
import com.enginizer.services.sas.client.spi.VerifySessionRequestDTO;
import com.enginizer.services.sas.client.spi.VerifySessionResponseDTO;
import com.enginizer.services.sas.v1.VerifySessionRequest;
import com.enginizer.services.sas.v1.VerifySessionResponse;
import org.springframework.ws.client.core.WebServiceTemplate;

/**
 * Handler for the SAS' verifySession operation.
 */
public class VerifySessionHandlerAdapter implements HandlerAdapter<VerifySessionRequestDTO, VerifySessionResponseDTO> {

    private WebServiceTemplate verifySessionServiceTemplate;
    private VerifySessionRequestConverter requestConverter;
    private VerifySessionResponseConverter responseConverter;

    public VerifySessionHandlerAdapter(WebServiceTemplate verifySessionServiceTemplate, VerifySessionRequestConverter requestConverter, VerifySessionResponseConverter responseConverter) {
        this.verifySessionServiceTemplate = verifySessionServiceTemplate;
        this.requestConverter = requestConverter;
        this.responseConverter = responseConverter;
    }

    @Override
    public VerifySessionResponseDTO adaptAndHandle(VerifySessionRequestDTO verifySessionRequestDTO) throws SASException {
        VerifySessionRequest request = requestConverter.from(verifySessionRequestDTO);
        VerifySessionResponse response = (VerifySessionResponse) verifySessionServiceTemplate.marshalSendAndReceive(request);
        return responseConverter.from(response);
    }
}
