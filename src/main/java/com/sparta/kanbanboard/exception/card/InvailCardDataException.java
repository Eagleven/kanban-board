package com.sparta.kanbanboard.exception.card;

import com.sparta.kanbanboard.common.ResponseCodeEnum;

public class InvailCardDataException extends CardException{
    public InvailCardDataException(ResponseCodeEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
    // 제목, 카드상태, 내용 필수 데이터가 존재하지 않는 경우

}
