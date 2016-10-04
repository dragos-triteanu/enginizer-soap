package com.enginizer.services.sas.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class for encoding the legitimationData.
 */
public class LegitimationDataEncoder {

    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    public static String encryptLegitimationPassword(final String legitimationData) {
        return encoder.encode(legitimationData);
    }

    public static boolean matches(final String pass, final String encodedPass){
        return encoder.matches(pass,encodedPass);
    }
}
