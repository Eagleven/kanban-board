package com.sparta.kanbanboard.domain.board.service;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import static com.sparta.kanbanboard.domain.user.utils.Role.MANAGER;
import com.sparta.kanbanboard.domain.board.dto.BoardRequestDto;
import com.sparta.kanbanboard.domain.board.dto.BoardResponseDto;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.board.repository.BoardAdapter;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.domain.userandboard.entity.UserAndBoard;
import com.sparta.kanbanboard.domain.userandboard.repository.UserAndBoardAdapter;
import com.sparta.kanbanboard.exception.board.BoardAlreadyDeletedException;
import com.sparta.kanbanboard.exception.board.BoardForbiddenException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private static final int PAGE_SIZE = 10;
    private final BoardAdapter boardAdapter;
    private final UserAndBoardAdapter userAndBoardAdapter;


    // 보드 생성
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {
        if (!user.getUserRole().equals(MANAGER)) { // 매니저 권한이 없으면 보드 생성 불가
            throw new BoardForbiddenException(ResponseExceptionEnum.FORBIDDEN_CREATE_BOARD);
        }
        Board board = boardAdapter.save(
                new Board(requestDto.getName(), requestDto.getExplanation(), user));
        UserAndBoard userAndBoard = new UserAndBoard(board, user); // userAndBoard 엔티티에 기록 남김
        userAndBoardAdapter.save(userAndBoard);
        return new BoardResponseDto(board);
    }

    // 보드 리스트 조회
    public Page<BoardResponseDto> getBoardList(int page, User user) {
        Sort sort = Sort.by(Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

        List<UserAndBoard> userAndBoardList = userAndBoardAdapter.findByUserId(user.getId());
        List<Long> boardIdList = userAndBoardList.stream()
                .map(userAndBoard -> userAndBoard.getBoard().getId())
                .collect(Collectors.toList());

        Page<Board> boardList = boardAdapter.findByIdIn(boardIdList, pageable);
        return boardList.map(BoardResponseDto::new);
    }


    // 보드 수정
    @Transactional
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto, User user) {
        Board board = boardAdapter.findById(boardId);

        if (board.getStatus().equals(CommonStatusEnum.DELETED)) {
            throw new BoardAlreadyDeletedException(ResponseExceptionEnum.BOARD_ALREADY_DELETED);
        }

        UserAndBoard userAndBoard = userAndBoardAdapter.findByUserIdAndBoardId(user.getId(),
                boardId);
        if (!user.getUserRole().equals(MANAGER)) {
            throw new BoardForbiddenException(ResponseExceptionEnum.FORBIDDEN_UPDATE_BOARD);
        }

        board.update(requestDto);
        return new BoardResponseDto(board);
    }

    @Transactional
    public void deleteBoard(Long boardId, User user) {
        // 보드가 존재하는지 확인
        Board board = boardAdapter.findById(boardId);

        // 삭제 처리된 보드인지 확인
        if (board.getStatus().equals(CommonStatusEnum.DELETED)) {
            throw new BoardAlreadyDeletedException(ResponseExceptionEnum.BOARD_ALREADY_DELETED);
        }

        // 보드에 참여중인지 확인
        UserAndBoard userAndBoard = userAndBoardAdapter.findByUserIdAndBoardId(user.getId(),
                boardId);
        if (!user.getUserRole().equals(MANAGER)) {
            throw new BoardForbiddenException(ResponseExceptionEnum.FORBIDDEN_DELETE_BOARD);
        }

        board.delete();
    }
}
