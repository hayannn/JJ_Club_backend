package com.jjclub.auth.domain;

import java.time.LocalDateTime;
import lombok.*;
import com.jjclub.auth.dto.UserUpdateDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "nickname")
    private String nickName;

    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "user_email", length = 45)
    @JoinTable(name = "email_validation_code",
        joinColumns = {@JoinColumn(name="email",referencedColumnName = "email")})
    private String email;
    @Column(name = "user_password")
    private String password;
    @Column(length = 45)
    private String mbti;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @ManyToMany
    @Column
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name="user_id",referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_status",referencedColumnName = "authority_status")})
    private Set<Authority> authorities = new HashSet<>();

    @Builder
    public User(String email, String userName, String nickName, String phoneNumber, String password, String mbti, Set<Authority> authorities, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.userName = userName;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.mbti = mbti;
        this.authorities = authorities;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public void updateMember(UserUpdateDto dto, PasswordEncoder passwordEncoder) {
        if(dto.getPassword() != null) this.password = passwordEncoder.encode(dto.getPassword());
        if(dto.getUserName() != null) this.userName = dto.getUserName();
        if(dto.getNickName() != null) this.nickName = dto.getNickName();
        if(dto.getPhoneNumber() != null) this.nickName = dto.getPhoneNumber();
        if(dto.getMbti() != null) this.mbti = dto.getMbti();
    }
}
