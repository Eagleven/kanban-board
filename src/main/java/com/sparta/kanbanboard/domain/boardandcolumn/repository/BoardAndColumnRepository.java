package com.sparta.kanbanboard.domain.boardandcolumn.repository;

import com.sparta.kanbanboard.domain.boardandcolumn.entity.BoardAndColumn;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardAndColumnRepository extends JpaRepository<BoardAndColumn, Long> {

    List<BoardAndColumn> findByColumnId(Long columnId);
}
