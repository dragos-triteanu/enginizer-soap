package com.enginizer.services.sas.model;

/**
 * Possible values for keys received as signature data when requesting an MTAN.
 */
public enum SignatureDataKey {

    AMOUNT("AMOUNT"),
    ACCOUNT("ACCOUNT");

    private String keyName;

    SignatureDataKey(String keyName) {
        this.keyName = keyName;
    }

    public static SignatureDataKey fromString(String value) {
        if (value != null) {
            for (SignatureDataKey signatureKey: SignatureDataKey.values()) {
                if (value.equalsIgnoreCase(signatureKey.getKeyName())) {
                    return signatureKey;
                }
            }
        }
        return null;
    }

    public String getKeyName() {
        return keyName;
    }
}
