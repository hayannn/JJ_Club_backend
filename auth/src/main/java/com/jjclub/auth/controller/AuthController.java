package com.jjclub.auth.controller;

import com.jjclub.auth.dto.EmailVerifyDto;
import com.jjclub.auth.service.EmailService;
import com.jjclub.auth.service.ResponseService;
import com.jjclub.auth.service.exception.EmailValidCodeException;
import com.jjclub.auth.web.SingleResult;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.jjclub.auth.dto.UserRequestDto;
import com.jjclub.auth.dto.UserResponseDto;
import com.jjclub.auth.dto.TokenResponseDto;
import com.jjclub.auth.dto.TokenRequestDto;
import com.jjclub.auth.service.AuthService;
import com.jjclub.auth.web.ApiResponse;
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
public class AuthController {
    private final AuthService authService;
    private final ResponseService responseService;
    private final EmailService emailService;

    // 회원가입
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponseDto> signup(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ApiResponse.createSuccess(authService.signup(userRequestDto));
    }

    /**
     * 이메일 인증을 위한 코드를 보내는 API
     * @param email
     *      인증할 Email을 인자로 받음
     * @throws Exception
     */
    @PostMapping("/email")
    public SingleResult<String> sendValidationCode(@RequestBody Map<String, String> email) throws Exception{
        log.info(email.get("email"));
        emailService.sendSimpleMessage(email.get("email"));

        return responseService.getSingleResult("send mail");
    }

    /**
     * 이메일 인증 확인을 위한 API
     * @param emailVerifyDTO
     *      email, code를 인자로 받는다.
     */
    @PostMapping("/verifyCode") // 이메일 인증 코드 검증
    public SingleResult<?> verifyCode(@RequestBody EmailVerifyDto emailVerifyDTO) {
        boolean verifingCode = emailService.verifyEmailCode(emailVerifyDTO.getEmail(), emailVerifyDTO.getCode());
        log.info("인증여부: " + verifingCode);

        if(verifingCode){
            return responseService.getSingleResult("true");
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
    public SingleResult<?> refreshValidationCode(@RequestBody Map<String, String> email) throws Exception{
        emailService.refreshVerifyEmailCode(email.get("email"));
        return responseService.getSingleResult("reissue Email");
    }

    // 로그인
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TokenResponseDto> login(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ApiResponse.createSuccess(authService.login(userRequestDto));
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TokenResponseDto> reissue(@Valid @RequestBody TokenRequestDto tokenRequestDto) {
        return ApiResponse.createSuccess(authService.reissue(tokenRequestDto));
    }
}
