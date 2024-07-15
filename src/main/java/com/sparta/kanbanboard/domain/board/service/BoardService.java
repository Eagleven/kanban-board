package com.sparta.kanbanboard.domain.board.service;

import static com.sparta.kanbanboard.domain.user.utils.Role.MANAGER;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.board.dto.BoardRequestDto;
import com.sparta.kanbanboard.domain.board.dto.BoardResponseDto;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.board.repository.BoardAdapter;
import com.sparta.kanbanboard.domain.column.repository.ColumnRepository;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.domain.user.repository.UserAdapter;
import com.sparta.kanbanboard.domain.userandboard.entity.UserAndBoard;
import com.sparta.kanbanboard.domain.userandboard.repository.UserAndBoardAdapter;
import com.sparta.kanbanboard.exception.board.BoardAlreadyDeletedException;
import com.sparta.kanbanboard.exception.board.BoardForbiddenException;
import com.sparta.kanbanboard.exception.userandboard.UserAlreadyBoardMemberException;
import com.sparta.kanbanboard.exception.userandboard.UserNotBoardMemberException;
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
    private final UserAdapter userAdapter;

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

        Page<Board> boardList = boardAdapter.findByIdIn(boardIdList, pageable, CommonStatusEnum.ACTIVE);
        return boardList.map(BoardResponseDto::new);
    }

    // 보드 수정
    @Transactional
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto, User user) {
        // 보드가 존재하는지 확인
        Board board = boardAdapter.findById(boardId);

        // manager 권한인지 확인
        if (!user.getUserRole().equals(MANAGER)) {
            throw new BoardForbiddenException(ResponseExceptionEnum.FORBIDDEN_UPDATE_BOARD);
        }

        // 삭제 처리된 보드인지 확인
        if (board.getStatus().equals(CommonStatusEnum.DELETED)) {
            throw new BoardAlreadyDeletedException(ResponseExceptionEnum.BOARD_ALREADY_DELETED);
        }

        // 사용자가 보드에 참여중인지 확인 -> userAndBoard에 없으면 예외 처리
        if(!userAndBoardAdapter.existsByUserIdAndBoardId(user.getId(), boardId)){
            throw new UserNotBoardMemberException(ResponseExceptionEnum.USER_NOT_BOARD_MEMBER);
        }

        board.update(requestDto);
        return new BoardResponseDto(board);
    }

    @Transactional
    public void deleteBoard(Long boardId, User user) {
        // 보드가 존재하는지 확인
        Board board = boardAdapter.findById(boardId);

        // manager 권한인지 확인
        if (!user.getUserRole().equals(MANAGER)) {
            throw new BoardForbiddenException(ResponseExceptionEnum.FORBIDDEN_DELETE_BOARD);
        }

        // 삭제 처리된 보드인지 확인
        if (board.getStatus().equals(CommonStatusEnum.DELETED)) {
            throw new BoardAlreadyDeletedException(ResponseExceptionEnum.BOARD_ALREADY_DELETED);
        }

        // 사용자가 보드에 참여중인지 확인 -> userAndBoard에 없으면 예외 처리
        if(!userAndBoardAdapter.existsByUserIdAndBoardId(user.getId(), boardId)){
            throw new UserNotBoardMemberException(ResponseExceptionEnum.USER_NOT_BOARD_MEMBER);
        }
        boardAdapter.delete(board);
    }

    @Transactional
    public void inviteBoard(Long boardId, Long userId, User user) {
        // 초대할 사용자 정보를 가져옴
        User invitedUser = userAdapter.findById(user.getId());

        // 보드가 존재하는지 확인
        Board board = boardAdapter.findById(boardId);

        // manager 권한인지 확인
        if (!user.getUserRole().equals(MANAGER)) {
            throw new BoardForbiddenException(ResponseExceptionEnum.FORBIDDEN_INVITE_BOARD);
        }

        // 삭제 처리된 보드인지 확인
        if (board.getStatus().equals(CommonStatusEnum.DELETED)) {
            throw new BoardAlreadyDeletedException(ResponseExceptionEnum.BOARD_ALREADY_DELETED);
        }

        // 이미 보드에 초대된 사용자인 경우 예외 처리
        if(userAndBoardAdapter.existsByUserIdAndBoardId(userId, boardId)){
            throw new UserAlreadyBoardMemberException(ResponseExceptionEnum.USER_ALREADY_BOARD_MEMBER);
        }

        UserAndBoard newUserAndBoard = new UserAndBoard(board, invitedUser);
        userAndBoardAdapter.save(newUserAndBoard);
    }
}
