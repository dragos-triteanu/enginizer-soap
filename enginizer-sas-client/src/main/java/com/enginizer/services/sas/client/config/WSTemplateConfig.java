package com.enginizer.services.sas.client.config;

import com.enginizer.services.sas.client.interceptor.SASClientInterceptor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

/**
 * Configuration of the {@link org.springframework.ws.client.core.WebServiceTemplate}.
 */
@Configuration
public class WSTemplateConfig {

    @Value("${sas.baseurl}")
    private String sasBaseUrl;

    @Value("${sas.endpoint.path}")
    private String sasEndpointPath;

    @Bean(name = "sasTemplate")
    public WebServiceTemplate sasTemplate() {
        final WebServiceTemplate webServiceTemplate = authorizeWebServiceTemplate();
        webServiceTemplate.setDefaultUri(sasBaseUrl + sasEndpointPath);
        return webServiceTemplate;
    }

    public WebServiceTemplate authorizeWebServiceTemplate() {
        final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setMarshaller(jaxb2Marshaller());
        webServiceTemplate.setUnmarshaller(jaxb2Marshaller());
        webServiceTemplate.setCheckConnectionForFault(true);
        final ClientInterceptor[] interceptors = { sasClientInterceptor() };
        webServiceTemplate.setInterceptors(interceptors);
        return webServiceTemplate;
    }

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPaths(new String[]{"com.enginizer.services.sas.v1"});
        jaxb2Marshaller.setCheckForXmlRootElement(false);
        return jaxb2Marshaller;
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();

        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        return mappingJackson2HttpMessageConverter;
    }

    @Bean
    public ClientInterceptor sasClientInterceptor(){
        return new SASClientInterceptor();
    }


}
