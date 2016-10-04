package com.enginizer.services.sas.client.spi;

/**
 * DTO for modeling the {@link com.enginizer.services.sas.v1.VerifySessionRequest} at domain level.
 */
public class VerifySessionRequestDTO {
    private final String sessionToken;

    private VerifySessionRequestDTO(Builder builder) {
        this.sessionToken = builder.sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public static class Builder {
        private final String sessionToken;

        public Builder(String sessionToken) {
            this.sessionToken = sessionToken;
        }

        public VerifySessionRequestDTO build() {
            return new VerifySessionRequestDTO(this);
        }
    }
}
