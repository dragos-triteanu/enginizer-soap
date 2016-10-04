package com.enginizer.services.sas.endpoint;

import com.enginizer.services.sas.exception.SASServiceException;
import com.enginizer.services.sas.logging.SASPayloadLoggingInterceptor;
import com.enginizer.services.sas.services.AuthenticationService;
import com.enginizer.services.sas.services.MTANService;
import com.enginizer.services.sas.services.SessionService;
import com.enginizer.services.sas.v1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;

import javax.servlet.http.HttpServletRequest;

/**
 * Endpoint which handles the incoming requests and delegates to the service implementation for a response.
 */
@Endpoint
public class SASEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(SASEndpoint.class);
    static final String NAMESPACE_URI = "http://www.enginizer.com/services/sas/v1";

    private AuthenticationService authenticationService;
    private MTANService mtanService;
    private SessionService sessionService;

    @Autowired
    public SASEndpoint(final AuthenticationService authenticationService,
                       final MTANService mtanService,
                       final SessionService sessionService) {
        this.authenticationService = authenticationService;
        this.mtanService = mtanService;
        this.sessionService = sessionService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "loginRequest")
    @ResponsePayload
    public LoginResponse login(@RequestPayload LoginRequest request, MessageContext messageContext) {
        LoginResponse response = new LoginResponse();
        try {
            response = authenticationService.login(request);
            response.setCorrelationId((String) messageContext.getProperty(SASPayloadLoggingInterceptor.CORRELATION_ID));
        } catch (Exception e) {
            handleException(messageContext, e);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "logoutRequest")
    @ResponsePayload
    public LogoutResponse logout(@RequestPayload LogoutRequest request, MessageContext messageContext) {
        LogoutResponse response = new LogoutResponse();
        try {
            response = authenticationService.logout(request);
            response.setCorrelationId((String) messageContext.getProperty(SASPayloadLoggingInterceptor.CORRELATION_ID));
        } catch (Exception e) {
            handleException(messageContext, e);
        }
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "verifySessionRequest")
    @ResponsePayload
    public VerifySessionResponse verifySession(@RequestPayload VerifySessionRequest request, MessageContext messageContext) {
        VerifySessionResponse response = new VerifySessionResponse();
        try {
            response = sessionService.verifySession(request);
            response.setCorrelationId((String) messageContext.getProperty(SASPayloadLoggingInterceptor.CORRELATION_ID));
        } catch (Exception e) {
            handleException(messageContext, e);
        }
        return response;
    }


    /**
     * Returns the current request's IP from the TransportContextHolder.
     *
     * @return ip of the client.
     */
    private String getCurrentRequestIp() {
        TransportContext context = TransportContextHolder.getTransportContext();
        HttpServletConnection connection = (HttpServletConnection) context.getConnection();
        HttpServletRequest request = connection.getHttpServletRequest();
        String ipAddress = request.getRemoteAddr();
        return ipAddress;
    }

    /**
     * HConverts any domain level exception, to a {@link SASServiceException} and also logs the error.
     * A custom error tag is built, for easy log tracking of the request.
     *
     * @param messageContext the message context
     * @param e              the thrown exception.
     */
    private void handleException(MessageContext messageContext, Exception e) {
        String errorTag = "Error(" + messageContext.getProperty(SASPayloadLoggingInterceptor.CORRELATION_ID) + ")";
        LOG.error(errorTag + ": ", e);
        throw new SASServiceException(errorTag);
    }

}