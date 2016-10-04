package com.enginizer.services.sas.client.spi;

/**
 *
 */
public class UserIdDTO {
    private final String principalType;
    private final String id;
    private final String language;
    private final String authenticationToken;

    public String getPrincipalType() {
        return principalType;
    }

    public String getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    private UserIdDTO(Builder builder) {
        this.principalType = builder.principalType;
        this.id = builder.id;
        this.language = builder.language;
        this.authenticationToken = builder.authenticationToken;
    }

    public static class Builder {
        private final String principalType;
        private final String id;
        private String language;
        private String authenticationToken;

        public Builder(String principalType, String id) {
            this.principalType = principalType;
            this.id = id;
        }

        public Builder withLanguage(String language){
            this.language = language;
            return this;
        }

        public Builder withAuthenticationToken(String authenticationToken){
            this.authenticationToken = authenticationToken;
            return this;
        }

        public UserIdDTO build() {
            return new UserIdDTO(this);
        }
    }

}
