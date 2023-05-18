package com.jjclub.auth.dto;

import lombok.*;
import com.jjclub.auth.domain.Authority;
import com.jjclub.auth.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    private String email;
    private String password;
    private String userName;
    private String mbti;
    private Set<Authority> authorities;

    public User toMember(PasswordEncoder passwordEncoder, Set<Authority> authorities) {
        return User.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .userName(userName)
            .mbti(mbti)
            .authorities(authorities)
            .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}