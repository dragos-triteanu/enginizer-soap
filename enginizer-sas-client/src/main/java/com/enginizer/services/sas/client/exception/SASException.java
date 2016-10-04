package com.enginizer.services.sas.client.exception;

/**
 * Created by cs94749 on 26.04.2016.
 */
public class SASException extends RuntimeException {

    private int errorCode;
    private int errorCount;

    public SASException(Throwable throwable){
        super(throwable);
    }


    public SASException(String message) {
        super(message);
    }

    public SASException(final String message, final int errorCode, final int errorCount) {
        super(errorCode + ":" + message);
        this.errorCode = errorCode;
        this.errorCount = errorCount;
    }


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }
}
