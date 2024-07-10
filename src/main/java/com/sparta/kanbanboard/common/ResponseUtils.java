package com.sparta.kanbanboard.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {

    // 에러 상황일 때 사용
    public static ResponseEntity<HttpResponseDto> of(ResponseExceptionEnum responseCodeEnum) {
        return ResponseEntity.status(responseCodeEnum.getHttpStatus())
                .body(new HttpResponseDto(
                        responseCodeEnum.getHttpStatus(), responseCodeEnum.getMessage()
                ));
    }

    // 데이터 없이 상태코드와 메세지만 리턴할 때 사용
    public static ResponseEntity<HttpResponseDto> of(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus)
                .body(new HttpResponseDto(
                        httpStatus, message
                ));
    }

    // 상태코드와 메세지와 데이터를 함께 리턴할 때 사용
    public static <T> ResponseEntity<HttpResponseDto> of(HttpStatus httpStatus, String message, T data) {
        return ResponseEntity.status(httpStatus)
                .body(new HttpResponseDto(
                        httpStatus, message, data
                ));
    }
}