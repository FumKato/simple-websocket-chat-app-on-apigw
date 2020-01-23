package com.github.fumkato;

import com.amazonaws.services.lambda.runtime.Context;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;

public class OnConnectHandler extends AbstractWebSocketHandler{

  public Response handleRequest(Map<String, Object> input, Context context) {
    String connectionId = getConnectionId(input);
    String tableName = System.getenv(TABLE_NAME_ENV);

    Map<String, AttributeValue> items = new HashMap<>();
    AttributeValue value = AttributeValue.builder()
            .s(connectionId)
            .build();
    items.put("connectionId", value);
    PutItemRequest request = PutItemRequest.builder()
            .tableName(tableName)
            .item(items)
            .build();

    DynamoDbClient client = DynamoDbClient.builder()
            .region(Region.AP_NORTHEAST_1)
            .build();
    client.putItem(request);

    Response response = new Response();
    response.setStatusCode(200);
    response.setBody("Connection succeed");
    return response;
  }
}
