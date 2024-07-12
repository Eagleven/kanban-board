package com.sparta.kanbanboard.domain.board.service;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.board.dto.BoardRequestDto;
import com.sparta.kanbanboard.domain.board.dto.BoardResponseDto;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.board.repository.BoardAdapter;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.domain.user.utils.Role;
import com.sparta.kanbanboard.exception.board.BoardAlreadyDeletedException;
import com.sparta.kanbanboard.exception.board.BoardForbiddenException;
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


    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {
        if (!user.getUserRole().equals(Role.ADMIN)) {
            throw new BoardForbiddenException(ResponseExceptionEnum.FORBIDDEN_CREATE_BOARD);
        }
        Board board = new Board(requestDto.getName(), requestDto.getExplanation(), user);
        return new BoardResponseDto(boardAdapter.save(board));
    }

    public Page<BoardResponseDto> getBoardList(int page, User user) {
        Sort sort =Sort.by(Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

        Page<Board> boardList = boardAdapter.findAllByUserId(user.getId(), pageable);
        return null; // 사용자 정보가 필요해서 추후에 개발
        // user, admin 각각 어떻게 가져올것인가.. 고민 -> 내가 주인인 보드와 내가 참여중인 보드를 같이 조회해야되잖아...
    }


    @Transactional
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto, User user) {
        Board board = boardAdapter.findById(boardId);
        if (board.getStatus().equals(CommonStatusEnum.DELETED)) {
            throw new BoardAlreadyDeletedException(ResponseExceptionEnum.BOARD_ALREADY_DELETED);
        }

        if(board.getUser().getId().equals(user.getId()) && user.getUserRole().equals(Role.ADMIN))
        // 요청한 사용자가 해당 보드의 생성자인지 확인하는 로직 필요]


        // 삭제 처리된 보드인지 확인


        board.update(requestDto);
        return new BoardResponseDto(board);
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        // 요청한 사용자가 해당 보드의 생성자인지 확인하는 로직 필요

        Board board = boardAdapter.findById(boardId);

        // 삭제 처리된 보드인지 확인
        if (board.getStatus().equals(CommonStatusEnum.DELETED)) {
            throw new BoardAlreadyDeletedException(ResponseExceptionEnum.BOARD_ALREADY_DELETED);
        }
        board.delete();
    }
}
