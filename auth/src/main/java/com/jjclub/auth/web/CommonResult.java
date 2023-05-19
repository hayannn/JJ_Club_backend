package com.jjclub.auth.web;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {
    // 응답성공 여부 T/F
    private boolean success;

    // 응답코드 >=0 정상, < 0 비정상
    private int code;

    // 응답 메시지
    private String msg;
}
