package com.github.fumkato;

import com.amazonaws.services.lambda.runtime.Context;

import java.util.Map;

public class OnDisconnectHandler extends AbstractWebSocketHandler{
  @Override
  public Response handleRequest(Map<String, Object> input, Context context) {
    return null;
  }
}
