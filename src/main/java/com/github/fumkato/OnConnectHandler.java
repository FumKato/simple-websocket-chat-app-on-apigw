package com.github.fumkato

public class OnConnectHandler{
  public Response handleRequest(Map<String, Object> input, Context context) {
    
  }
  
  private String getConnectionId(Map<String, Object> input) {
    String endpointStr = System.getenv(WEBSOCKET_ENDPOINT_ENV);
    if (endpointStr == null) {
      return null;
    }

    try {
      URI endpoint = new URI(endpointStr);
      apiGwClient = ApiGatewayManagementApiClient.builder().endpointOverride(endpoint).build();
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
