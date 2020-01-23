package com.github.fumkato;

import com.amazonaws.services.lambda.runtime.Context;
import software.amazon.awssdk.services.apigatewaymanagementapi.ApiGatewayManagementApiClient;

import java.net.URI;
import java.util.Map;

public abstract class AbstractWebSocketHandler {
    protected static final String TABLE_NAME_ENV = "TABLE_NAME";

    public abstract Response handleRequest(Map<String, Object> input, Context context);

    protected String getConnectionId(Map<String, Object> input) {
        Object requestContext = input.get("requestContext");
        if (!(requestContext instanceof Map)) {
            return null;
        }

        return ((Map<String, String>) requestContext).get("connectionId");
    }
}
