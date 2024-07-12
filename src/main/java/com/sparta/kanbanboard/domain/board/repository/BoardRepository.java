package com.sparta.kanbanboard.domain.board.repository;

import com.sparta.kanbanboard.domain.board.entity.Board;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findByIdIn(List<Long> boardIdList, Pageable pageable);

}
