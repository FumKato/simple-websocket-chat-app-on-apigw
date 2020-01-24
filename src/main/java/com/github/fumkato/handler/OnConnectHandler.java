package com.github.fumkato.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.github.fumkato.model.Response;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;

public class OnConnectHandler extends AbstractWebSocketHandler {

  public Response handleRequest(Map<String, Object> input, Context context) {
    String connectionId = getConnectionId(input);
    String tableName = System.getenv(TABLE_NAME_ENV);

    System.out.println("Start method. table name: " + tableName);

    DynamoDbClient client = DynamoDbClient.builder()
            .region(Region.AP_NORTHEAST_1)
            .build();

    Map<String, AttributeValue> items = new HashMap<>();
    AttributeValue value = AttributeValue.builder()
            .s(connectionId)
            .build();
    items.put("connectionId", value);
    PutItemRequest request = PutItemRequest.builder()
            .tableName(tableName)
            .item(items)
            .build();
    System.out.println("connectionId: " + connectionId);

    System.out.println("Register");
    client.putItem(request);

    System.out.println("Done");

    Response response = new Response();
    response.setStatusCode(200);
    response.setBody("Connection succeed");
    return response;
  }
}
