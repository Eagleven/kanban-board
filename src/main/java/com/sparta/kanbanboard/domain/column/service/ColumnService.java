package com.sparta.kanbanboard.domain.column.service;

import static com.sparta.kanbanboard.domain.user.utils.Role.MANAGER;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.board.repository.BoardAdapter;
import com.sparta.kanbanboard.domain.column.dto.ColumnRequestDto;
import com.sparta.kanbanboard.domain.column.dto.ColumnResponseDto;
import com.sparta.kanbanboard.domain.column.entity.Column;
import com.sparta.kanbanboard.domain.column.repository.ColumnAdapter;
import com.sparta.kanbanboard.domain.column.repository.ColumnRepository;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.domain.userandboard.repository.UserAndBoardAdapter;
import com.sparta.kanbanboard.exception.board.BoardForbiddenException;
import com.sparta.kanbanboard.exception.column.ColumnForbiddenException;
import com.sparta.kanbanboard.exception.userandboard.UserNotBoardMemberException;
import java.util.List;
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
    private final BoardAdapter boardAdapter;
    private final UserAndBoardAdapter userAndBoardAdapter;
    private final ColumnRepository columnRepository;

    @Transactional
    public ColumnResponseDto create(Long boardId, ColumnRequestDto requestDto, User user) {
        // boardId 가 존재하는지 검사
        Board board = boardAdapter.findById(boardId);

        // 사용자가 보드에 참여중인지 확인 -> userAndBoard에 없으면 예외 처리
        if(!userAndBoardAdapter.existsByUserIdAndBoardId(user.getId(), boardId)){
            throw new UserNotBoardMemberException(ResponseExceptionEnum.USER_NOT_BOARD_MEMBER);
        }

        // 사용자 권한 조회
        if (!user.getUserRole().equals(MANAGER)) { // 매니저 권한이 없으면 칼럼 생성 불가
            throw new ColumnForbiddenException(ResponseExceptionEnum.FORBIDDEN_CREATE_COLUMN);
        }

        Column column = new Column(requestDto.getName(), user, board);

        Column lastColumn = columnRepository.findByBoardAndStatusAndNextIsNull(board, CommonStatusEnum.ACTIVE);
        Column savedColumn = columnAdapter.save(column);
        if (lastColumn != null) {
            lastColumn.setNext(savedColumn.getId());
            savedColumn.setPrev(lastColumn.getId());
            columnAdapter.save(lastColumn);
        }
        return ColumnResponseDto.of(columnAdapter.save(savedColumn));
    }

    public ColumnResponseDto get(Long boardId,Long columnId) {
        boardAdapter.findById(boardId);
        Column column = columnAdapter.findById(columnId);
        return ColumnResponseDto.of(column);
    }

    public List<ColumnResponseDto> getAll(Long boardId) {
        Board board = boardAdapter.findById(boardId);
        List<Column> columns = columnRepository.findByBoardAndStatus(board, CommonStatusEnum.ACTIVE);
        return columns.stream()
                .map(ColumnResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ColumnResponseDto update(Long boardId, Long columnId, ColumnRequestDto requestDto, User user) {
        // 사용자가 보드에 참여중인지 확인 -> userAndBoard에 없으면 예외 처리
        if(!userAndBoardAdapter.existsByUserIdAndBoardId(user.getId(), boardId)){
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
    public ColumnResponseDto delete(Long boardId, Long columnId, User user) {
        // 사용자가 보드에 참여중인지 확인 -> userAndBoard에 없으면 예외 처리
        if(!userAndBoardAdapter.existsByUserIdAndBoardId(user.getId(), boardId)){
            throw new UserNotBoardMemberException(ResponseExceptionEnum.USER_NOT_BOARD_MEMBER);
        }

        // 사용자 권한 조회
        if (!user.getUserRole().equals(MANAGER)) { // 매니저 권한이 없으면 칼럼 수정 불가
            throw new BoardForbiddenException(ResponseExceptionEnum.FORBIDDEN_UPDATE_COLUMN);
        }

        Column column = columnAdapter.findById(columnId);

        if (column.getPrev() != null) {
            Column prevColumn = columnAdapter.findById(column.getPrev());
            if (column.getNext() != null) {
                Column nextColumn = columnAdapter.findById(column.getNext());
                nextColumn.setPrev(column.getPrev());
                prevColumn.setNext(column.getNext());
                column.setNext(null);
                columnAdapter.save(nextColumn);
            } else {
                prevColumn.setNext(null);
            }
            column.setPrev(null);
            columnAdapter.save(prevColumn);
        } else if (column.getNext() != null) {
            Column nextColumn = columnAdapter.findById(column.getNext());
            nextColumn.setPrev(null);
            column.setNext(null);
            columnAdapter.save(nextColumn);
        }
        column.setStatus(CommonStatusEnum.DELETED);
        columnAdapter.save(column);
        return ColumnResponseDto.of(column);
    }
}