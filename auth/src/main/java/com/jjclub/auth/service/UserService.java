package com.jjclub.auth.service;

import lombok.RequiredArgsConstructor;
import com.jjclub.auth.domain.User;
import com.jjclub.auth.dto.UserResponseDto;
import com.jjclub.auth.dto.UserUpdateDto;
import com.jjclub.auth.repository.UserRepository;
import com.jjclub.auth.repository.RefreshTokenRepository;
import com.jjclub.auth.util.SecurityUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final StringRedisTemplate redisTemplate;

    /**
     * 내 정보 조회
     */
    @Transactional(readOnly = true)
    public UserResponseDto getMyInfo() {
        return userRepository.findById(SecurityUtil.getLoginMemberId())
                .map(UserResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

    /**
     * 내 정보 수정 (이메일 수정 불가)
     */
    @Transactional
    public void updateMyInfo(UserUpdateDto dto) {
        User user = userRepository
                .findById(SecurityUtil.getLoginMemberId())
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));

        user.updateMember(dto,passwordEncoder);
    }

    /**
     * 로그아웃
     */
    @Transactional
    public void logout(HttpServletRequest request) {

        String jwt = request.getHeader("Authorization").substring(7);
        ValueOperations<String, String> logoutValueOperations = redisTemplate.opsForValue();
        logoutValueOperations.set(jwt, jwt); // redis set 명령어

        refreshTokenRepository.deleteByKey(String.valueOf(SecurityUtil.getLoginMemberId()))
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteMember() {
        Long loginMemberId = SecurityUtil.getLoginMemberId();
        if (loginMemberId == null) {
            throw new RuntimeException("로그인 유저 정보가 없습니다.");
        }
        userRepository.deleteById(loginMemberId);
    }
}