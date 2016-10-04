package com.enginizer.services.sas.client.spi;

/**
 * DTO for modeling the {@link com.enginizer.services.sas.v1.LoginRequest} at domain level.
 */
public class LoginRequestDTO {

    private final UserIdDTO user;
    private final LegitimationDTO legitimation;
    private final String frontendID;

    private LoginRequestDTO(Builder builder) {
        this.user = builder.user;
        this.legitimation = builder.legitimation;
        this.frontendID = builder.frontendID;
    }

    public static class Builder {
        // Required parameters
        private final UserIdDTO user;
        private final LegitimationDTO legitimation;
        private final String frontendID;

        public Builder(final UserIdDTO user, final LegitimationDTO legitimation, final String frontendID) {
            this.user = user;
            this.legitimation = legitimation;
            this.frontendID = frontendID;
        }

        public LoginRequestDTO build() {
            return new LoginRequestDTO(this);
        }
    }

    public UserIdDTO getUser() {
        return user;
    }

    public LegitimationDTO getLegitimation() {
        return legitimation;
    }

    public String getFrontendID() {
        return frontendID;
    }

}
