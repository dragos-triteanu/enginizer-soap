package com.enginizer.services.sas.client.converter;

/**
 * Defines a general contract for the converter api.
 */
public interface Converter <FromObject,ToObject> {

    ToObject from(final FromObject fromObject);
}
