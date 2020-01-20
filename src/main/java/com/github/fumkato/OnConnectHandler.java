package com.github.fumkato;

import com.amazonaws.services.lambda.runtime.Context;
import java.util.Map;

public class OnConnectHandler extends AbstractWebSocketHandler{

  public Response handleRequest(Map<String, Object> input, Context context) {
    String connectionId = getConnectionId(input);
    return null;
  }
}
