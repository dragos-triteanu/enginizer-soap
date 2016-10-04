package com.enginizer.services.sas.client.spi;


/**
 * DTO class for modeling a {@link com.enginizer.services.sas.v1.VerifySessionResponse} at domain level.
 */
public class VerifySessionResponseDTO extends ReplyDTO {
    private final boolean isValid;

    public VerifySessionResponseDTO(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }
}
