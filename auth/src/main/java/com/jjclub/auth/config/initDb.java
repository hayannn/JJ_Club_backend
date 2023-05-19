package com.jjclub.auth.config;

import lombok.RequiredArgsConstructor;
import com.jjclub.auth.domain.Authority;
import com.jjclub.auth.domain.AuthorityEnum;
import com.jjclub.auth.repository.AuthorityRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * INSERT INTO authority (AUTHORITY_STATUS) values ('ROLE_USER');
 */
@Component
@RequiredArgsConstructor
public class initDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final AuthorityRepository authorityRepository;

        public void dbInit() {
            authorityRepository.save(new Authority(AuthorityEnum.ROLE_USER));
        }
    }
}


