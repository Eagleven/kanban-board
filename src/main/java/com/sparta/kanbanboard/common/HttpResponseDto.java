package com.sparta.kanbanboard.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpResponseDto {

    private Integer statusCode;
    private String message;
    private Object data;

    public HttpResponseDto(ResponseCodeEnum responseCodeEnum, Object data) {
        this.statusCode = responseCodeEnum.getHttpStatus().value();
        this.message = responseCodeEnum.getMessage();
        this.data = data;
    }

    public HttpResponseDto(ResponseCodeEnum responseCodeEnum) {
        this.statusCode = responseCodeEnum.getHttpStatus().value();
        this.message = responseCodeEnum.getMessage();
    }

    public HttpResponseDto(ResponseExceptionEnum responseExceptionEnum) {
        this.statusCode = responseExceptionEnum.getHttpStatus().value();
        this.message = responseExceptionEnum.getMessage();
    }
}
