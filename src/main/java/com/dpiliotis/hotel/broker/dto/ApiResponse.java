package com.dpiliotis.hotel.broker.dto;

import java.util.List;

public class ApiResponse<T> {

  private T data;
  private List<ErrorDto> errors;

  public ApiResponse() { }

  public ApiResponse(T data) {
    this.data = data;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public List<ErrorDto> getErrors() {
    return errors;
  }

  public void setErrors(List<ErrorDto> errors) {
    this.errors = errors;
  }
}
