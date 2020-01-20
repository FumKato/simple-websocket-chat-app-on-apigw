package com.github.fumkato;

import com.amazonaws.services.lambda.runtime.Context;
import software.amazon.awssdk.services.apigatewaymanagementapi.ApiGatewayManagementApiClient;

import java.net.URI;
import java.util.Map;

public abstract class AbstractWebSocketHandler {
    private static final String WEBSOCKET_ENDPOINT_ENV = "WEBSOCKET_ENDPOINT";

    public abstract Response handleRequest(Map<String, Object> input, Context context);

    protected String getConnectionId(Map<String, Object> input) {
        String endpointStr = System.getenv(WEBSOCKET_ENDPOINT_ENV);
        if (endpointStr == null) {
            return null;
        }

        try {
            URI endpoint = new URI(endpointStr);
            ApiGatewayManagementApiClient apiGwClient = ApiGatewayManagementApiClient.builder().endpointOverride(endpoint).build();
        } catch (Exception e) {
            System.err.println("Failed to construct a client of API Gateway.");
            e.printStackTrace();
            return null;
        }

        Object requestContext = input.get("requestContext");
        if (!(requestContext instanceof Map)) {
            return null;
        }

        return ((Map<String, String>) requestContext).get("connectionId");
    }
}
