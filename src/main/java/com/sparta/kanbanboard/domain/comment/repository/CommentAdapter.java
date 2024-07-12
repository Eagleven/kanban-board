package com.sparta.kanbanboard.domain.comment.repository;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.comment.entity.Comment;
import com.sparta.kanbanboard.exception.comment.CommentNotFoundException;
import com.sparta.kanbanboard.exception.comment.CreateCommentFailureException;
import com.sparta.kanbanboard.exception.comment.DeleteCommentFailureException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentAdapter {

    private final CommentRepository commentRepository;

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(ResponseExceptionEnum.COMMENT_NOT_FOUND));
    }


    public List<Comment> getCardComments(Card card) {
        return commentRepository.findByCardAndStatus(card, CommonStatusEnum.ACTIVE);
    }

    public Comment save(Comment comment) {
        if (comment.getContent().trim().isEmpty() || comment.getCard().getContents().trim().isEmpty()) {
            throw new CreateCommentFailureException(ResponseExceptionEnum.CREATE_COMMENT_FAILURE);
        }
        return commentRepository.save(comment);
    }

    public Comment delete(Comment comment, String username) {
        if (!comment.getUser().getUsername().equals(username)) {
            throw new DeleteCommentFailureException(ResponseExceptionEnum.DELETE_COMMENT_FAILURE);
        }
        comment.setStatus(CommonStatusEnum.DELETED);
        return commentRepository.save(comment);
    }
}
