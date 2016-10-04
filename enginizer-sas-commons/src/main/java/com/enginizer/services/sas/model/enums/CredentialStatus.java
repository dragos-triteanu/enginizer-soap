package com.enginizer.services.sas.model.enums;

import com.enginizer.services.sas.model.entities.Credential;

/**
 * Possible values for {@link Credential}'s status.
 */
public enum CredentialStatus {

    ACTIVE("A"),
    EXPIRED("E"),
    MUST_CHANGE("C");

    private String value;

    CredentialStatus(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
