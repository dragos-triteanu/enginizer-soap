package com.enginizer.services.sas.client.spi;


/**
 * DTO for representing a {@link com.enginizer.services.sas.v1.LogoutRequest}.
 */
public class LogoutRequestDTO {
    private final String sessionToken;

    private LogoutRequestDTO(Builder builder) {
        this.sessionToken = builder.sessionToken;
    }

    public static class Builder {
        private final String sessionToken;

        public Builder(String sessionToken) {
            this.sessionToken = sessionToken;
        }

        public LogoutRequestDTO build() {
            return new LogoutRequestDTO(this);
        }
    }

    public String getSessionToken() {
        return sessionToken;
    }
}
