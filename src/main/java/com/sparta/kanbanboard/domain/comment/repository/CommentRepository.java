package com.sparta.kanbanboard.domain.comment.repository;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long>  {
    @Query("SELECT c FROM Comment c JOIN FETCH c.card WHERE c.card = :card AND c.status = :status ORDER BY c.createdAt DESC")
    List<Comment> findByCardAndStatusOrderByCreatedAtDesc(@Param("card") Card card, @Param("status") CommonStatusEnum status);
}
