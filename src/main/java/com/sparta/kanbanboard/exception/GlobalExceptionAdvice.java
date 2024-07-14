package com.sparta.kanbanboard.exception;

import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.common.ResponseUtils;
import com.sparta.kanbanboard.exception.board.BoardException;
import com.sparta.kanbanboard.exception.column.ColumnException;
import com.sparta.kanbanboard.exception.user.UserException;
import com.sparta.kanbanboard.exception.userandboard.UserAndBoardException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(BoardException.class)
    public ResponseEntity<HttpResponseDto> handleBoardException(BoardException e) {
        log.error("에러 메세지: ", e);
        return ResponseUtils.of(e.getResponseExceptionEnum());
    }

    @ExceptionHandler(UserAndBoardException.class)
    public ResponseEntity<HttpResponseDto> handleUserAndBoardException(UserAndBoardException e) {
        log.error("에러 메세지: ", e);
        return ResponseUtils.of(e.getResponseExceptionEnum());
    }

    @ExceptionHandler(ColumnException.class)
    public ResponseEntity<HttpResponseDto> handleColumnException(ColumnException e) {
        log.error("에러 메세지: ", e);
        return ResponseUtils.of(e.getResponseExceptionEnum());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpResponseDto> handleValidationExceptions(
            MethodArgumentNotValidException e) {
        List<String> errorMessageList = new ArrayList<>();
        e.getBindingResult().getAllErrors()
                .forEach(v -> errorMessageList.add(v.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new HttpResponseDto(HttpStatus.BAD_REQUEST.value(), "유효성 검사 실패",
                        errorMessageList));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<HttpResponseDto> handleUserException(UserException e) {
        log.error("에러 메세지: ", e);
        return ResponseUtils.of(e.getResponseCodeEnum());
    }
}