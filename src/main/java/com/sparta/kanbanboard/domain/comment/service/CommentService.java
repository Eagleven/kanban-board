package com.sparta.kanbanboard.domain.comment.service;

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
            List<Comment> comments = commentRepository.findByCard(card.get());
            return comments.stream()
                    .map(CommentResponseDto::new)
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }
//
//    public HttpResponseDto updateComment(Long commentId, CommentDto commentDto) {
//        Optional<Comment> optionalComment = commentRepository.findById(commentId);
//        if (optionalComment.isPresent()) {
//            Comment comment = optionalComment.get();
//            /*
//            * if (!user.getId().equals(comment.getUser().getId()) {
//            *   httpResponseDto = new HttpResponseDto(HttpStatus.NOT_FOUND, "적합하지 않은 접근입니다.");
//            * } else {
//            *
//            * }
//            * */
//            comment.updateComment(commentDto);
//            httpResponseDto = new HttpResponseDto(HttpStatus.OK, "댓글이 수정되었습니다.", comment);
//        } else {
//            httpResponseDto = new HttpResponseDto(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다.");
//        }
//        return httpResponseDto;
//    }
//
//    public HttpResponseDto deleteComment(Long commentId) {
//        Optional<Comment> optionalComment = commentRepository.findById(commentId);
//
//        if (optionalComment.isPresent()) {
//            Comment comment = optionalComment.get();
//            comment.setStatus("deleted");
//            httpResponseDto = new HttpResponseDto(HttpStatus.OK, "댓글이 삭제되었습니다.");
//        } else {
//            httpResponseDto = new HttpResponseDto(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다.");
//        }
//        return httpResponseDto;
//    }
}
