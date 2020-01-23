package com.github.fumkato;

import com.amazonaws.services.lambda.runtime.Context;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.apigatewaymanagementapi.ApiGatewayManagementApiClient;
import software.amazon.awssdk.services.apigatewaymanagementapi.model.PostToConnectionRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SendMessageHandler extends AbstractWebSocketHandler{
  private static final String WEBSOCKET_ENDPOINT_ENV = "WEBSOCKET_ENDPOINT";

  @Override
  public Response handleRequest(Map<String, Object> input, Context context) {
    final String webSocketEndpoint = System.getenv(WEBSOCKET_ENDPOINT_ENV);
    final String tableName = System.getenv(TABLE_NAME_ENV);

    Map<String, String> body = (Map)input.get("body");
    String message = body.get("Message");

    DynamoDbClient client = DynamoDbClient.builder()
            .region(Region.AP_NORTHEAST_1)
            .build();
    ScanRequest scanRequest = ScanRequest.builder()
            .tableName(tableName)
            .build();
    ScanResponse scanResponse = client.scan(scanRequest);

    try {
      ApiGatewayManagementApiClient apiGwClient = ApiGatewayManagementApiClient.builder()
              .endpointOverride(new URI(webSocketEndpoint))
              .build();
      for(Map<String, AttributeValue> item : scanResponse.items()) {
        PostToConnectionRequest postToConnectionRequest = PostToConnectionRequest.builder()
                .data(SdkBytes.fromString(message, StandardCharsets.UTF_8))
                .connectionId(item.get("connectionId").s())
                .build();
        apiGwClient.postToConnection(postToConnectionRequest);
      }
    } catch(URISyntaxException e) {
      System.err.println("Failed to create API GW client");
      e.printStackTrace();
      return null;
    }

    Response response = new Response();
    response.setStatusCode(200);
    response.setBody("Send message successfully");
    return response;
  }
}
