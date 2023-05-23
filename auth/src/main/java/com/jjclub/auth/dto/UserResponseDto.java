package com.jjclub.auth.dto;

import com.jjclub.auth.domain.EmailValidationCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.jjclub.auth.domain.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private String email;
    private String userName;
    private String nickName;

    private String phoneNumber;
    private String mbti;

    public static UserResponseDto of(User user) {
        return new UserResponseDto(
            user.getEmail(), user.getUserName(), user.getNickName(), user.getPhoneNumber(), user.getMbti());
    }

    public static EmailVerifyDto of(EmailValidationCode emailValidationCode) {
        return new EmailVerifyDto(
            emailValidationCode.getEmail(), emailValidationCode.getCode());
    }
}