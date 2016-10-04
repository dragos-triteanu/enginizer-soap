package com.enginizer.service.sas.integration;

import com.enginizer.service.sas.TestApplication;
import com.enginizer.services.sas.client.spi.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.util.Assert.notNull;

/**
 * Base integration test class.
 */
@SpringApplicationConfiguration(classes = TestApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest("server.port:0")
public class SASBaseClientTest {
    public static final Integer SUCCESS_STATUS_CODE = 0;
    public static final String TESTBOT_PRINCIPAL_USERNAME = "xxx";
    public static final String TESTBOT_CREDENTIAL_PASSWORD = "xxx";
    public static final String NON_EXISTENT_SESSION = "NonExistentSession";
    public static final String TESTBOT_CRM_ID = "999999999";

    @Autowired
    protected SASClient SASClient;

    protected LogoutResponseDTO performLogout() {
        LoginResponseDTO loginResponse = performLogin();
        LogoutRequestDTO logoutRequestDTO = new LogoutRequestDTO.Builder(loginResponse.getSessionToken()).build();

        LogoutResponseDTO logoutResponse = SASClient.logout(logoutRequestDTO);
        assertNotNull(logoutResponse);
        assertEquals(SUCCESS_STATUS_CODE,loginResponse.getReturnCode());
        return logoutResponse;
    }


    protected LoginResponseDTO performLogin() {
        LegitimationDTO legitimationDTO = new LegitimationDTO.Builder("PASSWORD", TESTBOT_CREDENTIAL_PASSWORD).build();
        UserIdDTO userIdDTO = new UserIdDTO.Builder("USERNAME", TESTBOT_PRINCIPAL_USERNAME).build();
        LoginRequestDTO requestDTO = new LoginRequestDTO.Builder(userIdDTO, legitimationDTO, "WEB").build();

        LoginResponseDTO loginResponse = SASClient.login(requestDTO);

        assertNotNull(loginResponse);
        assertEquals(SUCCESS_STATUS_CODE,loginResponse.getReturnCode());
        assertNotNull(loginResponse.getSessionToken());

        return loginResponse;
    }
}
