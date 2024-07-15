package com.sparta.kanbanboard.domain.board.repository;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.domain.board.entity.Board;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b FROM Board b WHERE b.id = :id AND b.status = :status")
    Optional<Board> findByIdAndStatus(@Param("id") Long id, @Param("status") CommonStatusEnum status);
    Page<Board> findByIdInAndStatus(List<Long> boardIdList, Pageable pageable, CommonStatusEnum status);
}
