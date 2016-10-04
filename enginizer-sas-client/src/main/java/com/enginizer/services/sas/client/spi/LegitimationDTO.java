package com.enginizer.services.sas.client.spi;

/**
 * DTO for modeling the {@link com.enginizer.services.sas.v1.Legitimation} at domain level.
 */
public class LegitimationDTO {
    private final String type;
    private final String data;

    private LegitimationDTO(Builder builder) {
        this.type = builder.type;
        this.data = builder.data;
    }

    public static class Builder {
        private final String type;
        private final String data;

        public Builder(String type, String data) {
            this.type = type;
            this.data = data;
        }

        public LegitimationDTO build() {
            return new LegitimationDTO(this);
        }
    }

    public String getType() {
        return type;
    }

    public String getData() {
        return data;
    }
}
