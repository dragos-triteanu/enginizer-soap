package com.enginizer.services.sas.model.enums;

import com.enginizer.services.sas.model.entities.User;

/**
 * Possible values for {@link User}'s status.
 */
public enum UserStatus {

    ACTIVE("A"),
    LOCKED("L"),
    PASSWORD_LOCKED("P");

    private String value;

    UserStatus(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
