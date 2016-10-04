package com.enginizer.services.sas.model;

/**
 * Possible service return codes.
 */
public enum ReturnCode {
    ACCEPTED(0, "Accepted"),

    INVALID_SESSIONID(100, "Specified session id does not exist"),

    DENIED_INVALID_USERID(101, "Denied because of invalid user"),
    DENIED_INVALID_PASSWORD(102, "Denied because of invalid password"),
    DENIED_USER_IS_LOCKED_TOO_MANY_WRONG_PASSWORDS(103, "Denied because number of maximum login failed attempts reached, user is now locked"),
    DENIED_INVALID_FRONTEND_ID(104, "Denied because the provided frontend id is unknown"),
    DENIED_USER_IS_LOCKED(105, "Denied because the user is locked"),
    DENIED_CREDENTIAL_EXPIRED(106, "Denied because the credential is expired"),
    DENIED_MUST_CHANGE_PASSWORD(107, "Denied must change password."),
    DENIED_INVALID_CREDENTIALS(108, "User does not have the required credentials."),
    DENIED_INVALID_PRINCIPALS(109, "Denied invalid principal id."),
    DENIED_INVALID_CREDENTIAL_FOR_FRONTEND(110, "Provided credential type not allowed on this frontend.");

    private final Integer code;
    private final String description;

    ReturnCode(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
