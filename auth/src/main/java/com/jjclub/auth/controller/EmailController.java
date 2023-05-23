package com.jjclub.auth.controller;

import com.jjclub.auth.dto.EmailVerifyDto;
import com.jjclub.auth.dto.UserRequestDto;
import com.jjclub.auth.dto.UserResponseDto;
import com.jjclub.auth.service.EmailService;

import com.jjclub.auth.service.exception.EmailValidCodeException;

import com.jjclub.auth.web.ApiResponse;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.jjclub.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class EmailController {
    private final AuthService authService;
    private final EmailService emailService;

    /**
     * 이메일 인증을 위한 코드를 보내는 API
     * @param email
     *      인증할 Email을 인자로 받음
     * @throws Exception
     */
    @PostMapping("/email")
    public ApiResponse<String> sendValidationCode(@RequestBody Map<String, String> email) throws Exception{
        log.info(email.get("email"));
        emailService.sendSimpleMessage(email.get("email"));

        return ApiResponse.createSuccess("send mail");
    }

    /**
     * 이메일 인증 확인을 위한 API
     * @param emailVerifyDTO
     *      email, code를 인자로 받는다.
     */
    @PostMapping("/verifyCode") // 이메일 인증 코드 검증
    public ApiResponse<?> verifyCode(@RequestBody EmailVerifyDto emailVerifyDTO) {
        boolean verifingCode = emailService.verifyEmailCode(emailVerifyDTO.getEmail(), emailVerifyDTO.getCode());
        log.info("인증여부: " + verifingCode);

        if(verifingCode){
            return ApiResponse.createSuccess("true");
        }
        else{
            throw new EmailValidCodeException();
        }
    }

    /**
     * 이메일 인증 코드 재발급 API
     * @param email
     *      재발급할 이메일을 parameter로 받음
     * @throws Exception
     */
    @PostMapping("/refreshCode")
    public ApiResponse<?> refreshValidationCode(@RequestBody Map<String, String> email) throws Exception{
        emailService.refreshVerifyEmailCode(email.get("email"));
        return ApiResponse.createSuccess("reissue Email");
    }
}
