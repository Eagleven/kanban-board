package com.sparta.kanbanboard.exception;

import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.common.ResponseUtils;
import com.sparta.kanbanboard.exception.user.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<HttpResponseDto> handleUserException(UserException e) {
        log.error("에러 메세지: ", e);
        return ResponseUtils.of(e.getResponseCodeEnum());
    }
}
