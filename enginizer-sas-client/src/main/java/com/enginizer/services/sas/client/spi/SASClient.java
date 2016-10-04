package com.enginizer.services.sas.client.spi;


import com.enginizer.services.sas.v1.VerifySessionRequest;
import com.enginizer.services.sas.v1.VerifySessionResponse;

/**
 * Authorization service's API..
 */
public interface SASClient {
    /**
     * Method used for authenticating a user, and creating a session in the SAS.
     *
     * @param loginRequest The login resuest object that contains the necessary data in order to perform authentication.
     * @return {@link LoginResponseDTO} with information about the status of the authentication.
     */
    LoginResponseDTO login(LoginRequestDTO loginRequest);

    /**
     * Performs a user's logout, by destroying the session assign to him.
     *
     * @param logoutRequest the request for logout.
     * @return 0 for success,other status code otherwise.
     */
    LogoutResponseDTO logout(LogoutRequestDTO logoutRequest);


    /**
     * Validates a user's session against the SAS.
     *
     * @param verifySessionRequest {@link VerifySessionRequest}.
     * @return {@link VerifySessionResponse}.
     */
    VerifySessionResponseDTO verifySession(VerifySessionRequestDTO verifySessionRequest);

}
