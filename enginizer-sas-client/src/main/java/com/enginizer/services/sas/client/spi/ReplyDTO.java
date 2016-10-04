package com.enginizer.services.sas.client.spi;

/**
 * Created by cs94749 on 26.04.2016.
 */
public class ReplyDTO {
    private String correlationId;
    private Integer returnCode;
    private String errorMessage;
    private String reasonCode;
    private Integer errorCounter;

    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public Integer getErrorCounter() {
        return errorCounter;
    }

    public void setErrorCounter(Integer errorCounter) {
        this.errorCounter = errorCounter;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}
