package com.sparta.kanbanboard.common;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

public class ResponseUtils {

    public static ResponseEntity<HttpResponseDto> of(ResponseCodeEnum responseCodeEnum){
        return ResponseEntity.status(responseCodeEnum.getHttpStatus())
                .body(new HttpResponseDto(responseCodeEnum));
    }

    public static ResponseEntity<HttpResponseDto> of(ResponseExceptionEnum responseExceptionEnum){
        return ResponseEntity.status(responseExceptionEnum.getHttpStatus())
                .body(new HttpResponseDto(responseExceptionEnum));
    }
    public static ResponseEntity<HttpResponseDto> of(ResponseCodeEnum responseCodeEnum, Object data){
        return ResponseEntity.status(responseCodeEnum.getHttpStatus())
                .body(new HttpResponseDto(responseCodeEnum, data));
    }
}
