package com.enginizer.services.sas.config;

import com.enginizer.services.sas.logging.SASPayloadInterceptor;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.mapping.PayloadRootAnnotationMethodEndpointMapping;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * Configuration class defining WS related beans.
 */
@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    private static final String EXPOSED_AT = "/services/sas/v1/*";
    private static final String PATH_TO_WSDL = "com/enginizer/services/sas/wsdl/SASV1.wsdl";
    private static final String PATH_TO_XSD = "com/enginizer/services/sas/xsd/SASV1.xsd";


    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, EXPOSED_AT);
    }

    @Bean(name = "SASV1")
    public SimpleWsdl11Definition simpleWsdl11Definition() {
        SimpleWsdl11Definition simpleWsdl11Definition = new SimpleWsdl11Definition();
        simpleWsdl11Definition.setWsdl(new ClassPathResource(PATH_TO_WSDL));
        return simpleWsdl11Definition;
    }

    @Bean(name = "SASV1Schema")
    public XsdSchema authenticationSchema() {
        return new SimpleXsdSchema(new ClassPathResource(PATH_TO_XSD));
    }


    @Bean
    public PayloadRootAnnotationMethodEndpointMapping payloadRootAnnotationMethodEndpointMapping() {
        PayloadRootAnnotationMethodEndpointMapping payloadRootAnnotationMethodEndpointMapping = new PayloadRootAnnotationMethodEndpointMapping();
        EndpointInterceptor[] interceptors = new EndpointInterceptor[2];
        interceptors[0] = payloadValidatingInterceptor();
        interceptors[1] = payloadLoggingInterceptor();
        payloadRootAnnotationMethodEndpointMapping.setInterceptors(interceptors);
        return payloadRootAnnotationMethodEndpointMapping;
    }

    @Bean
    public SASPayloadInterceptor payloadLoggingInterceptor() {
        return new SASPayloadInterceptor();
    }

    @Bean
    public PayloadValidatingInterceptor payloadValidatingInterceptor() {
        PayloadValidatingInterceptor payloadValidatingInterceptor = new PayloadValidatingInterceptor();
        payloadValidatingInterceptor.setSchema(new ClassPathResource("com/enginizer/services/sas/xsd/SASV1.xsd"));
        payloadValidatingInterceptor.setValidateRequest(true);
        payloadValidatingInterceptor.setValidateResponse(true);
        return payloadValidatingInterceptor;
    }

}