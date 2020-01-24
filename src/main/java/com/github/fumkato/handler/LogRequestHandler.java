package com.github.fumkato.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fumkato.model.Response;

import java.util.Map;

public class LogRequestHandler extends AbstractWebSocketHandler{
    @Override
    public Response handleRequest(Map<String, Object> input, Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(input));

        Response response = new Response();
        response.setStatusCode(200);
        response.setBody("Operation succeed");
        return response;
    }
}
