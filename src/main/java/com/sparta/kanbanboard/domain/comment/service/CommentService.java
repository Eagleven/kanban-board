package com.sparta.kanbanboard.domain.comment.service;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.card.repository.CardAdapter;
import com.sparta.kanbanboard.domain.comment.dto.CommentDto;
import com.sparta.kanbanboard.domain.comment.dto.CommentResponseDto;
import com.sparta.kanbanboard.domain.comment.entity.Comment;
import com.sparta.kanbanboard.domain.comment.repository.CommentAdapter;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.exception.comment.CreateCommentFailureException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CardAdapter cardAdapter;
    private final CommentAdapter commentAdapter;


    public CommentResponseDto createComment(Long cardId, CommentDto commentDto, User user) {
        Card card = cardAdapter.findById(cardId);
        Comment comment = new Comment(user, card, commentDto);
        return new CommentResponseDto(commentAdapter.save(comment));
    }

    public List<CommentResponseDto> getCommentsList(Long cardId) {
        Card card = cardAdapter.findById(cardId);

        List<Comment> comments = commentAdapter.getCardComments(card);
        return comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    public CommentResponseDto updateComment(Long commentId, CommentDto commentDto, String username) {
        Comment comment = commentAdapter.findById(commentId);
        if (!username.equals(comment.getUser().getUsername())) {
            throw new CreateCommentFailureException(ResponseExceptionEnum.CREATE_COMMENT_FAILURE);
        }
        comment.updateComment(commentDto);
        return new CommentResponseDto(commentAdapter.save(comment));
    }

    public void deleteComment(Long commentId, String username) {
        Comment comment = commentAdapter.findById(commentId);
        commentAdapter.delete(comment, username);
    }
}
