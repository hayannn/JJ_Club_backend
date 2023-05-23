package com.jjclub.auth.web;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AuthErrorMessage {
    EXIST_AUTH("101","exist AUTH"),

    EXIST_EMAIL("101","이미 가입되어 있는 이메일입니다."),

    NO_SUCH_AUTH("102","자격 증명에 실패하였습니다."),

    NO_MATCH_TOKEN("103","토큰의 유저 정보가 일치하지 않습니다.");


    private final String code;
    private final String message;
}

