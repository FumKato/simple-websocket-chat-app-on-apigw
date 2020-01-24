package com.github.fumkato.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fumkato.model.Response;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.apigatewaymanagementapi.ApiGatewayManagementApiClient;
import software.amazon.awssdk.services.apigatewaymanagementapi.model.GoneException;
import software.amazon.awssdk.services.apigatewaymanagementapi.model.PostToConnectionRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SendMessageHandler extends AbstractWebSocketHandler {
  private static final String WEBSOCKET_ENDPOINT_ENV = "WEBSOCKET_ENDPOINT";

  @Override
  public Response handleRequest(Map<String, Object> input, Context context) {
    final String webSocketEndpoint = System.getenv(WEBSOCKET_ENDPOINT_ENV);
    final String tableName = System.getenv(TABLE_NAME_ENV);

    ObjectMapper mapper = new ObjectMapper();
    Map<String, String> body;
    try {
      body = mapper.readValue((String)input.get("body"), new TypeReference<Map<String, String>>(){});
    } catch(JsonProcessingException e) {
      System.err.println("Failed to parse request body");
      e.printStackTrace();
      return null;
    }


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
        String connectionId = item.get("connectionId").s();
        try {
          PostToConnectionRequest postToConnectionRequest = PostToConnectionRequest.builder()
                  .data(SdkBytes.fromString(body.get("message"), StandardCharsets.UTF_8))
                  .connectionId(connectionId)
                  .build();
          apiGwClient.postToConnection(postToConnectionRequest);
        }catch(GoneException e) {
          Map<String, AttributeValue> items = new HashMap<>();
          AttributeValue value = AttributeValue.builder()
                  .s(connectionId)
                  .build();
          items.put("connectionId", value);
          DeleteItemRequest request = DeleteItemRequest.builder()
                  .tableName(tableName)
                  .key(items)
                  .build();
          client.deleteItem(request);
        }
      }
    } catch(URISyntaxException e) {
      System.err.println("Failed to create API GW client");
      e.printStackTrace();
    }

    Response response = new Response();
    response.setStatusCode(200);
    response.setBody("Send message successfully");
    return response;
  }
}
