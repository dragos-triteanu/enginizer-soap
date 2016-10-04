package com.enginizer.services.sas.exception;

/**
 * Exception to be thrown by the SAS service, in case of service level failure.
 */
public class SASServiceException extends RuntimeException {

    public SASServiceException(String message) {
        super(message);
    }

    public SASServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
