package com.enginizer.services.sas.client.interceptor;

import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import java.io.ByteArrayOutputStream;

/**
 * Interceptor class that resides in the client, with the purpose of logging the request/response that goes through the client.
 */
public class SASClientInterceptor implements ClientInterceptor {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(SASClientInterceptor.class);
    private static final String SOAP_REQUEST_PREFIX = "SOAP-Request: ";
    private static final String SOAP_RESPONSE_PREFIX = "SOAP-Response: ";
    private static final String SOAP_FAULT_PREFIX = "SOAP-Fault: ";

    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        logPayload(SOAP_REQUEST_PREFIX, (SaajSoapMessage) messageContext.getRequest());
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        logPayload(SOAP_RESPONSE_PREFIX, (SaajSoapMessage) messageContext.getResponse());
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        logPayload(SOAP_FAULT_PREFIX, (SaajSoapMessage) messageContext.getResponse());
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {

    }

    private void logPayload(final String origin, final SaajSoapMessage soapMessage) {
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            soapMessage.writeTo(bos);
            LOG.info(origin + bos.toString());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            // don't throw the exception
        }
    }

}
