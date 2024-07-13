package com.sparta.kanbanboard.domain.column.service;

import static com.sparta.kanbanboard.domain.user.utils.Role.MANAGER;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.board.repository.BoardAdapter;
import com.sparta.kanbanboard.domain.boardandcolumn.entity.BoardAndColumn;
import com.sparta.kanbanboard.domain.boardandcolumn.repository.BoardAndColumnAdapter;
import com.sparta.kanbanboard.domain.column.dto.ColumnRequestDto;
import com.sparta.kanbanboard.domain.column.dto.ColumnResponseDto;
import com.sparta.kanbanboard.domain.column.entity.Column;
import com.sparta.kanbanboard.domain.column.repository.ColumnAdapter;
import com.sparta.kanbanboard.domain.column.repository.ColumnRepository;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.domain.userandboard.entity.UserAndBoard;
import com.sparta.kanbanboard.domain.userandboard.repository.UserAndBoardAdapter;
import com.sparta.kanbanboard.exception.board.BoardForbiddenException;
import com.sparta.kanbanboard.exception.board.BoardNotFoundException;
import com.sparta.kanbanboard.exception.column.ColumnForbiddenException;
import com.sparta.kanbanboard.exception.userandboard.UserNotBoardMemberException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ColumnService {

    private final ColumnAdapter columnAdapter;
    private final BoardAndColumnAdapter boardAndColumnAdapter;
    private final BoardAdapter boardAdapter;
    private final UserAndBoardAdapter userAndBoardAdapter;

    private final ColumnRepository columnRepository;

    @Transactional
    public ColumnResponseDto create(Long boardId, ColumnRequestDto requestDto, User user) {
        // 사용자가 보드에 참여중인지 확인 -> userAndBoard에 없으면 예외 처리
        Optional<UserAndBoard> userAndBoard = userAndBoardAdapter.findByUserIdAndBoardId(
                user.getId(), boardId);
        if (userAndBoard.isEmpty()) {
            throw new UserNotBoardMemberException(ResponseExceptionEnum.USER_NOT_BOARD_MEMBER);
        }

        // 사용자 권한 조회
        if (!user.getUserRole().equals(MANAGER)) { // 매니저 권한이 없으면 칼럼 생성 불가
            throw new ColumnForbiddenException(ResponseExceptionEnum.FORBIDDEN_CREATE_COLUMN);
        }

        // boardId 가 존재하는지 검사
        Board board = boardAdapter.findById(boardId);

        // 가장 큰 시퀀스 값을 찾아서 1을 더해 설정 (삭제되지 않은 칼럼 기준)
        List<Column> columns = columnRepository.findAllByStatusOrderBySequenceAsc(CommonStatusEnum.ACTIVE);
        int maxSequence = columns.isEmpty() ? 0 : columns.get(columns.size() - 1).getSequence();
        maxSequence++;

        // request -> entity
        Column column = Column.toEntity(requestDto, maxSequence, user);
        Column savedColumn = columnAdapter.save(column);

        BoardAndColumn boardAndColumn = new BoardAndColumn(board, column);
        boardAndColumnAdapter.save(boardAndColumn);

        // entity -> response
        return ColumnResponseDto.of(savedColumn);
    }

    public ColumnResponseDto get(Long columnId) {
        Column column = columnAdapter.findById(columnId);
        return ColumnResponseDto.of(column);
    }

    public List<ColumnResponseDto> getAll() {
        List<Column> columns = columnAdapter.findAll();
        return columns.stream()
                .map(ColumnResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ColumnResponseDto update(Long columnId, ColumnRequestDto requestDto, User user) {
        // 칼럼 Id 를 통해 보드 Id 찾기
        List<BoardAndColumn> boardAndColumns = boardAndColumnAdapter.findByColumnId(columnId);
        Long boardId = boardAndColumns.stream()
                .map(boardAndColumn -> boardAndColumn.getBoard().getId())
                .findAny() // 첫 번째 요소를 가져옴
                .orElseThrow(() -> new BoardNotFoundException(ResponseExceptionEnum.BOARD_NOT_FOUND));

        // 사용자가 보드에 참여중인지 확인 -> userAndBoard에 없으면 예외 처리
        Optional<UserAndBoard> userAndBoard = userAndBoardAdapter.findByUserIdAndBoardId(
                user.getId(), boardId);
        if (userAndBoard.isEmpty()) {
            throw new UserNotBoardMemberException(ResponseExceptionEnum.USER_NOT_BOARD_MEMBER);
        }

        // 사용자 권한 조회
        if (!user.getUserRole().equals(MANAGER)) { // 매니저 권한이 없으면 칼럼 수정 불가
            throw new BoardForbiddenException(ResponseExceptionEnum.FORBIDDEN_UPDATE_COLUMN);
        }

        Column column = columnAdapter.findById(columnId);
        Column updatedColumn = column.update(requestDto);
        return ColumnResponseDto.of(updatedColumn);
    }

    @Transactional
    public ColumnResponseDto delete(Long columnId, User user) {
        // 칼럼 Id 를 통해 보드 Id 찾기
        List<BoardAndColumn> boardAndColumns = boardAndColumnAdapter.findByColumnId(columnId);
        Long boardId = boardAndColumns.stream()
                .map(boardAndColumn -> boardAndColumn.getBoard().getId())
                .findAny() // 첫 번째 요소를 가져옴
                .orElseThrow(() -> new BoardNotFoundException(ResponseExceptionEnum.BOARD_NOT_FOUND));

        // 사용자가 보드에 참여중인지 확인 -> userAndBoard에 없으면 예외 처리
        Optional<UserAndBoard> userAndBoard = userAndBoardAdapter.findByUserIdAndBoardId(
                user.getId(), boardId);
        if (userAndBoard.isEmpty()) {
            throw new UserNotBoardMemberException(ResponseExceptionEnum.USER_NOT_BOARD_MEMBER);
        }

        // 사용자 권한 조회
        if (!user.getUserRole().equals(MANAGER)) { // 매니저 권한이 없으면 칼럼 수정 불가
            throw new BoardForbiddenException(ResponseExceptionEnum.FORBIDDEN_UPDATE_COLUMN);
        }

        Column column = columnAdapter.findById(columnId);
        Column deletedColumn = column.delete();


        // 시퀀스 업데이트 (삭제되지 않은 칼럼 기준)
        int deletedSequence = deletedColumn.getSequence();
        List<Column> columnsToUpdate = columnRepository.findBySequenceGreaterThanAndStatusOrderBySequenceAsc(deletedSequence, CommonStatusEnum.ACTIVE);
        for (Column col : columnsToUpdate) {
            col.updateSequence(col.getSequence() - 1);
        }
        columnRepository.saveAll(columnsToUpdate);
        deletedColumn.updateSequence(-1);

        return ColumnResponseDto.of(deletedColumn);
    }
}
