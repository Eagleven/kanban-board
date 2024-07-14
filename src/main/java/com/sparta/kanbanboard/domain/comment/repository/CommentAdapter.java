package com.sparta.kanbanboard.domain.comment.repository;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.comment.entity.Comment;
import com.sparta.kanbanboard.exception.comment.CommentNotFoundException;
import com.sparta.kanbanboard.exception.comment.CreateCommentFailureException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentAdapter {

    private final CommentRepository commentRepository;

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .filter(comment -> comment.getStatus().equals(CommonStatusEnum.ACTIVE))
                .orElseThrow(() -> new CommentNotFoundException(ResponseExceptionEnum.COMMENT_NOT_FOUND));
    }

    public List<Comment> getCardComments(Card card) {
        return commentRepository.findByCardAndStatusOrderByCreatedAtDesc(card, CommonStatusEnum.ACTIVE);
    }

    public Comment save(Comment comment) {
        if (comment.getContent() == null || comment.getCard().getContents() == null) {
            throw new CreateCommentFailureException(ResponseExceptionEnum.CREATE_COMMENT_FAILURE);
        }
        return commentRepository.save(comment);
    }

    public Comment delete(Comment comment) {
        comment.setStatus(CommonStatusEnum.DELETED);
        return commentRepository.save(comment);
    }
}
