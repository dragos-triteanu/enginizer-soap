package com.enginizer.services.sas.util;

import com.mifmif.common.regex.Generex;

/**
 * Utility class for generating random credential data.
 */
public class CredentialDataGenerator {

    private static Generex generex;

    /**
     * Generates a random character sequence based on the received regex.
     *
     * @param credentialRegex regex based on which the credential data is generated
     * @return the generated credential data
     */
    public static String generateCredential(final String credentialRegex) {
        generex = new Generex(credentialRegex);
        return generex.random();
    }
}
