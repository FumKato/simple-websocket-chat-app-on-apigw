package com.github.fumkato;

public class Response{
  private int statusCode;
  private String body;

  public int getStatusCode() {
    return this.statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public String getBody() {
    return this.body;
  }

  public void setBody(String body) {
    this.body = body;
  }
}
