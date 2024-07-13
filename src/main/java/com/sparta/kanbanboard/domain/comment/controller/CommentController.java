package com.sparta.kanbanboard.domain.comment.controller;

import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.common.ResponseCodeEnum;
import com.sparta.kanbanboard.common.ResponseUtils;
import com.sparta.kanbanboard.common.security.details.UserDetailsImpl;
import com.sparta.kanbanboard.domain.comment.dto.CommentRequestDto;
import com.sparta.kanbanboard.domain.comment.dto.CommentResponseDto;
import com.sparta.kanbanboard.domain.comment.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{cardId}")
    public ResponseEntity<HttpResponseDto> createComment(@PathVariable("cardId") Long cardId, @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto result = commentService.createComment(cardId, commentRequestDto, userDetails.getUser());
        return ResponseUtils.of(ResponseCodeEnum.CREATE_COMMENT_SUCCESS, result);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<HttpResponseDto> getCommentsList(@PathVariable("cardId") Long cardId) {
        List<CommentResponseDto> comments = commentService.getCommentsList(cardId);
        return ResponseUtils.of(ResponseCodeEnum.GET_COMMENTS, comments);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<HttpResponseDto> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal
    UserDetailsImpl userDetails) {
        CommentResponseDto commentResponseDto = commentService.updateComment(commentId,
                commentRequestDto, userDetails.getUser());
        return ResponseUtils.of(ResponseCodeEnum.UPDATE_COMMENT_SUCCESS, commentResponseDto);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<HttpResponseDto> deleteComment(@PathVariable("commentId") Long commentId, @AuthenticationPrincipal
    UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId, userDetails.getUser());
        return ResponseUtils.of(ResponseCodeEnum.DELETE_COMMENT_SUCCESS);
    }
}
