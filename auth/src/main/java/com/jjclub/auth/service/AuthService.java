package com.jjclub.auth.service;

import lombok.RequiredArgsConstructor;
import com.jjclub.auth.domain.Authority;
import com.jjclub.auth.domain.AuthorityEnum;
import com.jjclub.auth.domain.User;
import com.jjclub.auth.domain.RefreshToken;
import com.jjclub.auth.dto.UserRequestDto;
import com.jjclub.auth.dto.UserResponseDto;
import com.jjclub.auth.dto.TokenResponseDto;
import com.jjclub.auth.dto.TokenRequestDto;
import com.jjclub.auth.jwt.TokenProvider;
import com.jjclub.auth.repository.AuthorityRepository;
import com.jjclub.auth.repository.UserRepository;
import com.jjclub.auth.repository.RefreshTokenRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 회원가입
     */
    @Transactional
    public UserResponseDto signup(UserRequestDto userRequestDto) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 이메일입니다.");
        }

        Authority authority = authorityRepository
                .findByAuthorityStatus(AuthorityEnum.ROLE_USER).orElseThrow(()->new RuntimeException("권한 정보가 없습니다."));

        Set<Authority> set = new HashSet<>();
        set.add(authority);

        User user = userRequestDto.toMember(passwordEncoder, set);
        return UserResponseDto.of(userRepository.save(user));
    }
    /**
     * 로그인
     */
    @Transactional
    public TokenResponseDto login(UserRequestDto userRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = userRequestDto.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenResponseDto tokenResponseDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenResponseDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenResponseDto;
    }

    /**
     * 재발급
     */
    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto) {
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenResponseDto tokenResponseDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenResponseDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenResponseDto;
    }
}