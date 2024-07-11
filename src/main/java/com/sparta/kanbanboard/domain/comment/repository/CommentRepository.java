package com.sparta.kanbanboard.domain.comment.repository;

import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>  {
    List<Comment> findByCard(Card card);
}
