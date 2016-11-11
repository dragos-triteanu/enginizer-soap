package com.enginizer.services.sas.logging;

import com.enginizer.services.sas.v1.TechnicalHeader;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.xml.transform.TransformerObjectSupport;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.UUID;

/**
 * Interceptor class used for logging the request/response exchanges in SAS.
 * The interceptor is also responsible for performing request correlation, by extracting header information in order to log.
 */
public class SASPayloadInterceptor extends TransformerObjectSupport implements EndpointInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(SASPayloadInterceptor.class);
    public static final String CORRELATION_ID = "correlationId";
    public static final QName TECHNICAL_DATA_CALL_QNAME = new QName("http://www.enginizer.com/services/sas/v1", "techHeader");

    private XmlMapper mapper = new XmlMapper();

    @Override
    public boolean handleRequest(MessageContext messageContext, Object o) throws Exception {
        TechnicalHeader technicalDataCall = extractOrCreateTechnicalDataHeaderFromRequest(messageContext);
        String correlationIdForRequest = (null == technicalDataCall || null == technicalDataCall.getRequestId()) ?
                UUID.randomUUID().toString() : technicalDataCall.getRequestId();

        messageContext.setProperty(CORRELATION_ID, correlationIdForRequest);


        if (this.isLogEnabled()) {
            this.logMessageSource("Request(" + messageContext.getProperty(CORRELATION_ID) + "): ", this.getSource(messageContext.getRequest()));
        }

        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object o) throws Exception {
        appendHeader(messageContext);
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

    /**
     * Extarcts a {@link Header}
     *
     * @param messageContext
     * @return
     * @throws TransformerException
     * @throws java.io.IOException
     */
    private TechnicalHeader extractOrCreateTechnicalDataHeaderFromRequest(MessageContext messageContext) throws TransformerException, java.io.IOException {
        TechnicalHeader tdc = null;
        SaajSoapMessage request = (SaajSoapMessage) messageContext.getRequest();
        SoapHeader header = request.getSoapHeader();


        try {
            tdc = extractTechnicalHeader(header);
        } catch (Exception e) {
            LOG.error("Could not extract technicalDataCall header from request", e);
        }

        if (null == tdc) {
            try {
                createNewHeader(header);

            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }

        return tdc;
    }

    /**
     * Creates a nerw TechnicalHeader and appends it to the current header element.
     * @param header the {@link SoapHeader}.
     * @throws JAXBException
     */
    private void createNewHeader(SoapHeader header) throws JAXBException {
        TechnicalHeader newHeader = new TechnicalHeader();
        newHeader.setRequestId(UUID.randomUUID().toString());

        JAXBContext context = JAXBContext.newInstance(TechnicalHeader.class);
        Marshaller marshaller = context.createMarshaller();

        marshaller.marshal(new JAXBElement(TECHNICAL_DATA_CALL_QNAME,TechnicalHeader.class,newHeader), header.getResult());
    }

    /**
     * Extracts the {@link TechnicalHeader} element from the request.
     * @param header the {@link SoapHeader}.
     * @return {@link TechnicalHeader}.
     * @throws TransformerException
     * @throws java.io.IOException
     */
    private TechnicalHeader extractTechnicalHeader(SoapHeader header) throws TransformerException, java.io.IOException {
        SoapHeaderElement technicalData = header.examineHeaderElements(TECHNICAL_DATA_CALL_QNAME).next();

        StreamResult result = new StreamResult(new StringWriter());
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(technicalData.getSource(), result);

        return mapper.readValue(result.getWriter().toString(), TechnicalHeader.class);
    }

    /**
     * Appends the header element from the request to the response.
     *
     * @param messageContext
     */
    public void appendHeader(MessageContext messageContext) {
        SaajSoapMessage soapRequest = (SaajSoapMessage) messageContext.getRequest();
        SoapHeader reqheader = soapRequest.getSoapHeader();
        SaajSoapMessage soapResponse = (SaajSoapMessage) messageContext.getResponse();
        SoapHeader respheader = soapResponse.getSoapHeader();

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Iterator<SoapHeaderElement> itr = reqheader.examineAllHeaderElements();
            while (itr.hasNext()) {
                SoapHeaderElement ele = itr.next();
                transformer.transform(ele.getSource(), respheader.getResult());
            }
        } catch (TransformerException e) {
            LOG.error("Error adding header from request to response", e);
        }
    }

    protected boolean isLogEnabled() {
        return LOG.isDebugEnabled();
    }

    protected Source getSource(WebServiceMessage message) {
        return message.getPayloadSource();
    }

}




