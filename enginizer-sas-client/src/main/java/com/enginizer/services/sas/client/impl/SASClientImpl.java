package com.enginizer.services.sas.client.impl;

import com.enginizer.services.sas.client.handler.LoginHandlerAdapter;
import com.enginizer.services.sas.client.handler.LogoutHandlerAdapter;
import com.enginizer.services.sas.client.handler.VerifySessionHandlerAdapter;
import com.enginizer.services.sas.client.spi.*;

/**
 * Implementation of the {@link SASClient} for communicating with the SAS in order to perform an authorization request.
 */
public class SASClientImpl implements SASClient {

    private LoginHandlerAdapter loginHandlerAdapter;
    private LogoutHandlerAdapter logoutHandlerAdapter;
    private VerifySessionHandlerAdapter verifySessionHandlerAdapter;

    public SASClientImpl(LoginHandlerAdapter loginHandlerAdapter,
                         LogoutHandlerAdapter logoutHandlerAdapter,
                         VerifySessionHandlerAdapter verifySessionHandlerAdapter) {
        this.loginHandlerAdapter = loginHandlerAdapter;
        this.logoutHandlerAdapter = logoutHandlerAdapter;
        this.verifySessionHandlerAdapter = verifySessionHandlerAdapter;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        return loginHandlerAdapter.adaptAndHandle(loginRequest);
    }

    @Override
    public LogoutResponseDTO logout(LogoutRequestDTO logoutRequest) {
        return logoutHandlerAdapter.adaptAndHandle(logoutRequest);
    }

    @Override
    public VerifySessionResponseDTO verifySession(VerifySessionRequestDTO verifySessionRequestDTO) {
        return verifySessionHandlerAdapter.adaptAndHandle(verifySessionRequestDTO);
    }

}
