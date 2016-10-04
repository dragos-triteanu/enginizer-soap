package com.enginizer.services.sas.client.handler;

import com.enginizer.services.sas.client.exception.SASException;

/**
 * Created by cs94749 on 26.04.2016.
 */
public interface HandlerAdapter<HandlerRequest,HandlerResponse> {

    HandlerResponse adaptAndHandle(final HandlerRequest request) throws SASException;
}
