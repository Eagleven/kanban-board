package com.sparta.kanbanboard.domain.board.repository;

import com.sparta.kanbanboard.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
