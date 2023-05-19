package com.jjclub.auth.dto;

import com.jjclub.auth.domain.EmailValidationCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class EmailValidateDto {
    private LocalDateTime createTime;

    public EmailValidationCode toEntity(String email, String emailCode){
        return EmailValidationCode.builder()
            .code(emailCode)
            .email(email)
            .createTime(LocalDateTime.now())
            .build();
    }

}