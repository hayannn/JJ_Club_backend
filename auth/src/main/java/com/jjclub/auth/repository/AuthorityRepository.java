package com.jjclub.auth.repository;

import com.jjclub.auth.domain.Authority;
import com.jjclub.auth.domain.AuthorityEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, AuthorityEnum> {
    Optional<Authority> findByAuthorityStatus(AuthorityEnum authorityStatus);
}