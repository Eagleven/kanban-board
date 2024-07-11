package com.sparta.kanbanboard.domain.comment.service;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.card.repository.CardRepository;
import com.sparta.kanbanboard.domain.comment.dto.CommentDto;
import com.sparta.kanbanboard.domain.comment.dto.CommentResponseDto;
import com.sparta.kanbanboard.domain.comment.entity.Comment;
import com.sparta.kanbanboard.domain.comment.repository.CommentAdapter;
import com.sparta.kanbanboard.domain.comment.repository.CommentRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentAdapter commentAdapter;
    private final CardRepository cardRepository;


    public CommentResponseDto createComment(Long cardId, CommentDto commentDto) {
        Optional<Card> cardOptional = cardRepository.findById(cardId);
        if (cardOptional.isPresent()) {
            Card card = cardOptional.get();
            if (card.getContent() == null) {
                return null;
            }

            // User user = userDetails.getUser();
            // Comment comment = new Comment(user, card, commentDto);
            Comment comment = new Comment();

            return new CommentResponseDto(commentAdapter.save(comment));
        } else {
            return null;
        }
    }

    public List<CommentResponseDto> getCommentsList(Long cardId) {
        Optional<Card> card = cardRepository.findById(cardId);
        if (card.isPresent()) {
            List<Comment> comments = commentRepository.findByCardAndStatus(card.get(), CommonStatusEnum.ACTIVE);
            return comments.stream()
                    .map(CommentResponseDto::new)
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    public CommentResponseDto updateComment(Long commentId, CommentDto commentDto) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            if (commentDto.getContent() == null) {
                return null;
            }
            comment.updateComment(commentDto);
            return new CommentResponseDto(commentAdapter.save(comment));
        } else {
            return null;
        }
    }

    public Comment deleteComment(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setStatus(CommonStatusEnum.DELETED);
            return commentAdapter.save(comment);
        } else {
            return null;
        }
    }
}
