package com.sparta.kanbanboard.domain.comment.controller;

import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.common.ResponseCodeEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.common.ResponseUtils;
import com.sparta.kanbanboard.domain.comment.dto.CommentDto;
import com.sparta.kanbanboard.domain.comment.dto.CommentResponseDto;
import com.sparta.kanbanboard.domain.comment.entity.Comment;
import com.sparta.kanbanboard.domain.comment.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{cardId}")
    public ResponseEntity<HttpResponseDto> createComment(@PathVariable("cardId") Long cardId, @RequestBody CommentDto commentDto) {
        CommentResponseDto result = commentService.createComment(cardId, commentDto);
        if (result == null) {
            return ResponseUtils.of(ResponseExceptionEnum.CREATE_COMMENT_FAILURE);
        }
        return ResponseUtils.of(ResponseCodeEnum.CREATE_COMMENT_SUCCESS, result);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<HttpResponseDto> getCommentsList(@PathVariable("cardId") Long cardId) {
        List<CommentResponseDto> result = commentService.getCommentsList(cardId);
        if (result == null) {
            return ResponseUtils.of(ResponseExceptionEnum.CARD_NOT_FOUND);
        }
        return ResponseUtils.of(ResponseCodeEnum.GET_COMMENTS, result);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<HttpResponseDto> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentDto commentDto) {
        CommentResponseDto result = commentService.updateComment(commentId, commentDto);
        if (result == null) {
            if (commentDto.getContent() == null) {
                return ResponseUtils.of(ResponseExceptionEnum.COMMENT_CONTENT_REQUIRED);
            } else {
                return ResponseUtils.of(ResponseExceptionEnum.COMMENT_NOT_FOUND);
            }
        }

        return ResponseUtils.of(ResponseCodeEnum.UPDATE_COMMENT_SUCCESS, result);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<HttpResponseDto> deleteComment(@PathVariable("commentId") Long commentId) {
        Comment result = commentService.deleteComment(commentId);
        if (result == null) {
            return ResponseUtils.of(ResponseExceptionEnum.COMMENT_NOT_FOUND);
        }

        return ResponseUtils.of(ResponseCodeEnum.DELETE_COMMENT_SUCCESS);
    }
}
