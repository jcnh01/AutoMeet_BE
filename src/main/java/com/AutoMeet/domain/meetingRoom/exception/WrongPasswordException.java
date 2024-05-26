package com.AutoMeet.domain.meetingRoom.exception;


import com.AutoMeet.global.exception.ErrorCode;
import com.AutoMeet.global.exception.ServiceException;

public class WrongPasswordException extends ServiceException {

    private static final ErrorCode ERROR_CODE = ErrorCode.WRONG_PASSWORD;
    private static final String MESSAGE_KEY = "비밀번호를 잘못 입력하셨습니다.";

    public WrongPasswordException(String meetingId) {
        super(ERROR_CODE, MESSAGE_KEY, new Object[] {meetingId});
    }
}