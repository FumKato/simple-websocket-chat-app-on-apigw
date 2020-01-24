package com.github.fumkato.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fumkato.model.Response;

import java.util.Map;

public abstract class AbstractWebSocketHandler {
    protected static final String TABLE_NAME_ENV = "TABLE_NAME";

    public abstract Response handleRequest(Map<String, Object> input, Context context) throws JsonProcessingException;

    protected String getConnectionId(Map<String, Object> input) {
        Object requestContext = input.get("requestContext");
        if (!(requestContext instanceof Map)) {
            return null;
        }

        return ((Map<String, String>) requestContext).get("connectionId");
    }
}
