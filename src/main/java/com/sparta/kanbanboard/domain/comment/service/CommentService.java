package com.sparta.kanbanboard.domain.comment.service;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.card.repository.CardAdapter;
import com.sparta.kanbanboard.domain.comment.dto.CommentRequestDto;
import com.sparta.kanbanboard.domain.comment.dto.CommentResponseDto;
import com.sparta.kanbanboard.domain.comment.entity.Comment;
import com.sparta.kanbanboard.domain.comment.repository.CommentAdapter;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.domain.userandboard.entity.UserAndBoard;
import com.sparta.kanbanboard.domain.userandboard.repository.UserAndBoardAdapter;
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
    private final UserAndBoardAdapter userAndBoardAdapter;

    public CommentResponseDto createComment(Long cardId, CommentRequestDto commentRequestDto, User user) {
        Card card = cardAdapter.findById(cardId);
        Comment comment = new Comment(user, card, commentRequestDto);
        return new CommentResponseDto(commentAdapter.save(comment));
    }

    public List<CommentResponseDto> getCommentsList(Long cardId) {
        Card card = cardAdapter.findById(cardId);

        List<Comment> comments = commentAdapter.getCardComments(card);
        return comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = commentAdapter.findById(commentId);
        if (!user.getUsername().equals(comment.getUser().getUsername())) {
            throw new CreateCommentFailureException(ResponseExceptionEnum.CREATE_COMMENT_FAILURE);
        }
        comment.updateComment(commentRequestDto);
        return new CommentResponseDto(commentAdapter.save(comment));
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = commentAdapter.findById(commentId);
        commentAdapter.delete(comment, user);
    }
}
