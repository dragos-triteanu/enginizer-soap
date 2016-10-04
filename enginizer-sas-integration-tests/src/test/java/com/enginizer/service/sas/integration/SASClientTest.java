package com.enginizer.service.sas.integration;

import com.enginizer.services.sas.client.spi.LoginResponseDTO;
import com.enginizer.services.sas.client.spi.VerifySessionRequestDTO;
import com.enginizer.services.sas.client.spi.VerifySessionResponseDTO;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.util.Assert.notNull;

/**
 * Functional integration tests for the SAS's operations.
 * <p>
 * For the moment, the testbot user will be used for the integration tests. (testbot - Pass)
 * After the create user service exists, we can proceed with tests that create users.
 */
public class SASClientTest extends SASBaseClientTest {
    private static final Logger LOG = LoggerFactory.getLogger(SASBaseClientTest.class);

    @Test
    public void shouldLogin() {
        performLogin();
        performLogout();
    }


    @Test
    public void shouldVerifySessionAsTrue() {
        LoginResponseDTO loginResponse = performLogin();

        VerifySessionRequestDTO dto = new VerifySessionRequestDTO.Builder(loginResponse.getSessionToken()).build();
        VerifySessionResponseDTO verifySessionResponse = SASClient.verifySession(dto);
        notNull(verifySessionResponse);
        assertEquals(true, verifySessionResponse.isValid());

        performLogout();
    }

    @Test
    public void shouldVerifySessionAsFalse() {
        VerifySessionRequestDTO dto = new VerifySessionRequestDTO.Builder(NON_EXISTENT_SESSION).build();
        VerifySessionResponseDTO verifySessionResponse = SASClient.verifySession(dto);
        notNull(verifySessionResponse);
        assertEquals(false, verifySessionResponse.isValid());
    }


}
