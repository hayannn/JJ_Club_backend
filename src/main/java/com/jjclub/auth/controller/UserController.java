package com.jjclub.auth.controller;

import lombok.RequiredArgsConstructor;
import com.jjclub.auth.dto.UserResponseDto;
import com.jjclub.auth.dto.UserUpdateDto;
import com.jjclub.auth.service.UserService;
import com.jjclub.auth.web.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/user")
@PreAuthorize("isAuthenticated()")
public class UserController {

    private final UserService userService;

    // 정보 조회
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponseDto> getMyInfo() {
        return ApiResponse.createSuccess(userService.getMyInfo());
    }

    // 정보 수정
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponseDto> updateMyInfo(@RequestBody UserUpdateDto dto) {
        userService.updateMyInfo(dto);
        return ApiResponse.createSuccess(userService.getMyInfo());
    }

    // 회원 탈퇴
    @DeleteMapping("/me2")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> deleteMember(HttpServletRequest request) {
        userService.logout(request);
        userService.deleteMember();
        return ApiResponse.createSuccessWithNoContent();
    }

    // 로그아웃
    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> logout(HttpServletRequest request) {
        userService.logout(request);
        return ApiResponse.createSuccessWithNoContent();
    }
}