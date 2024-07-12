package com.sparta.kanbanboard.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class HttpResponseDto {

    private Integer statusCode;
    private String message;
    private Object data;

    public HttpResponseDto(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public HttpResponseDto(Integer statusCode, String message, Object data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public HttpResponseDto(HttpStatus httpStatus, String s, Object data) {
        this.statusCode = httpStatus.value();
        this.message = s;
        this.data = data;
    }
}
