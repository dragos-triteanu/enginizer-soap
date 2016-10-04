package com.enginizer.services.sas.model;

/**
 * Services for which MTAN can be used for authorization.
 */
public enum ServiceName {

    DOMESTIC_PAYMENTS("domesticPayment");

    private String serviceName;

    ServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public static ServiceName fromString(String value) {
        if (value != null) {
            for (ServiceName service: ServiceName.values()) {
                if (value.equalsIgnoreCase(service.getServiceName())) {
                    return service;
                }
            }
        }
        return null;
    }
}
