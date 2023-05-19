package com.jjclub.auth.repository;

import com.jjclub.auth.domain.EmailValidationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailValidateCodeRepository extends JpaRepository<EmailValidationCode, Long> {
    Optional<EmailValidationCode> findByEmail(String email);

    void deleteByEmail(String email);

}
