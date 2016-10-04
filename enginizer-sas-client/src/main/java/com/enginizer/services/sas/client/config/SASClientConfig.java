package com.enginizer.services.sas.client.config;

import com.enginizer.services.sas.client.converter.request.LoginRequestConverter;
import com.enginizer.services.sas.client.converter.request.LogoutRequestConverter;
import com.enginizer.services.sas.client.converter.request.VerifySessionRequestConverter;
import com.enginizer.services.sas.client.converter.response.LoginResponseConverter;
import com.enginizer.services.sas.client.converter.response.LogoutResponseConverter;
import com.enginizer.services.sas.client.converter.response.VerifySessionResponseConverter;
import com.enginizer.services.sas.client.handler.LogoutHandlerAdapter;
import com.enginizer.services.sas.client.handler.VerifySessionHandlerAdapter;
import com.enginizer.services.sas.client.impl.SASClientImpl;
import com.enginizer.services.sas.client.spi.SASClient;
import com.enginizer.services.sas.client.handler.LoginHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.ws.client.core.WebServiceTemplate;

/**
 * Config class for the SAS client.
 */
@Configuration
@Import(WSTemplateConfig.class)
public class SASClientConfig {

    @Autowired
    private WebServiceTemplate sasTemplate;

    @Bean(name = "sasServiceClient")
    public SASClient authorizationService() {
        return new SASClientImpl(
                loginHandlerAdapter(),
                logoutHandlerAdapter(),
                verifySessionHandlerAdapter());
    }

    @Bean
    public VerifySessionHandlerAdapter verifySessionHandlerAdapter() {
        return new VerifySessionHandlerAdapter(sasTemplate, new VerifySessionRequestConverter(), new VerifySessionResponseConverter());
    }

    @Bean
    public LogoutHandlerAdapter logoutHandlerAdapter() {
        return new LogoutHandlerAdapter(sasTemplate, new LogoutRequestConverter(), new LogoutResponseConverter());
    }


    @Bean
    public LoginHandlerAdapter loginHandlerAdapter() {
        return new LoginHandlerAdapter(sasTemplate, new LoginRequestConverter(), new LoginResponseConverter());
    }

}
