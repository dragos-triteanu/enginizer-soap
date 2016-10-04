package com.enginizer.services.sas.client.handler;

import com.enginizer.services.sas.client.converter.request.LoginRequestConverter;
import com.enginizer.services.sas.client.converter.response.LoginResponseConverter;
import com.enginizer.services.sas.client.exception.SASException;
import com.enginizer.services.sas.client.spi.LoginRequestDTO;
import com.enginizer.services.sas.client.spi.LoginResponseDTO;
import com.enginizer.services.sas.v1.LoginRequest;
import com.enginizer.services.sas.v1.LoginResponse;
import org.springframework.ws.client.core.WebServiceTemplate;

/**
 * Handler class for converting and handling the requests for the SAS login operation.
 */
public class LoginHandlerAdapter implements HandlerAdapter<LoginRequestDTO,LoginResponseDTO> {
    private LoginRequestConverter loginRequestConverter;
    private LoginResponseConverter loginResponseConverter;
    private WebServiceTemplate loginServiceTemplate;

    public LoginHandlerAdapter(WebServiceTemplate loginServiceTemplate, LoginRequestConverter loginRequestConverter,LoginResponseConverter loginResponseConverter){
        this.loginServiceTemplate = loginServiceTemplate;
        this.loginRequestConverter = loginRequestConverter;
        this.loginResponseConverter = loginResponseConverter;
    }

    @Override
    public LoginResponseDTO adaptAndHandle(LoginRequestDTO loginRequestDTO) throws SASException {
        LoginRequest request = loginRequestConverter.from(loginRequestDTO);
        LoginResponse response = (LoginResponse) loginServiceTemplate.marshalSendAndReceive(request);
        return loginResponseConverter.from(response);
    }
}
