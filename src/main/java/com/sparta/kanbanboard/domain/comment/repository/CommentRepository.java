package com.sparta.kanbanboard.domain.comment.repository;

import com.sparta.kanbanboard.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>  {

}
