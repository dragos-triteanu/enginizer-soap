package com.enginizer.services.sas.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.xml.transform.TransformerObjectSupport;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.UUID;

/**
 * Interceptor for logging the request/response payload exchange in SOAP format.
 */
public class SASPayloadLoggingInterceptor extends TransformerObjectSupport implements EndpointInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(SASPayloadLoggingInterceptor.class);
    public static final String CORRELATION_ID = "correlationId";


    @Override
    public boolean handleRequest(MessageContext messageContext, Object o) throws Exception {
        messageContext.setProperty(CORRELATION_ID, UUID.randomUUID().toString());

        if (this.isLogEnabled()) {
            this.logMessageSource("Request(" + messageContext.getProperty(CORRELATION_ID) + "): ", this.getSource(messageContext.getRequest()));
        }

        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object o) throws Exception {
        if (this.isLogEnabled()) {
            this.logMessageSource("Response(" + messageContext.getProperty(CORRELATION_ID) + "): ", this.getSource(messageContext.getResponse()));
        }

        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object o) throws Exception {
        return false;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object o, Exception e) throws Exception {
    }

    private Transformer createNonIndentingTransformer() throws TransformerConfigurationException {
        Transformer transformer = this.createTransformer();
        transformer.setOutputProperty("omit-xml-declaration", "yes");
        transformer.setOutputProperty("indent", "no");
        return transformer;
    }

    protected void logMessageSource(String logMessage, Source source) throws TransformerException {
        if (source != null) {
            Transformer transformer = this.createNonIndentingTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(source, new StreamResult(writer));
            String message = logMessage + writer.toString();
            LOG.info(message);
        }

    }

    protected boolean isLogEnabled() {
        return LOG.isDebugEnabled();
    }

    protected Source getSource(WebServiceMessage message) {
        return message.getPayloadSource();
    }

}
