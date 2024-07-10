package com.sparta.kanbanboard.common;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

public class ResponseUtils {

    public static ResponseEntity<HttpResponseDto> of(ResponseExceptionEnum responseCodeEnum) {
        return ResponseEntity.status(responseCodeEnum.getHttpStatus())
                .body(new HttpResponseDto(
                        responseCodeEnum.getHttpStatus(), responseCodeEnum.getMessage()
                ));
    }

    public static ResponseEntity<HttpResponseDto> of(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus)
                .body(new HttpResponseDto(
                        httpStatus, message
                ));
    }

    public static <T> ResponseEntity<HttpResponseDto> of(HttpStatus httpStatus, String message, Object data) {
        return ResponseEntity.status(httpStatus)
                .body(new HttpResponseDto(
                        httpStatus, message, data
                ));
    }
}
