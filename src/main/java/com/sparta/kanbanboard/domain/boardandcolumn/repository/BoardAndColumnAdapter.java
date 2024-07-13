package com.sparta.kanbanboard.domain.boardandcolumn.repository;

import com.sparta.kanbanboard.domain.boardandcolumn.entity.BoardAndColumn;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardAndColumnAdapter {

    private final BoardAndColumnRepository repository;

    public void save(BoardAndColumn boardAndColumn) {
        repository.save(boardAndColumn);
    }

    public List<BoardAndColumn> findByColumnId(Long columnId) {
        return repository.findByColumnId(columnId);
    }
}
