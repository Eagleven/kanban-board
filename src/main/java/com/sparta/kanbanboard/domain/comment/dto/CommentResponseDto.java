package com.sparta.kanbanboard.domain.comment.dto;

import com.sparta.kanbanboard.domain.comment.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private String username;
    private String content;

    public CommentResponseDto(Comment comment) {
        this.username = comment.getUser().getUsername();
        this.content = getContent();
    }
}
