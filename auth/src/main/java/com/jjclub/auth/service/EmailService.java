package com.jjclub.auth.service;

import javax.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    String createKey();
    void sendSimpleMessage(String email) throws Exception;
    boolean verifyEmailCode(String email, String code);
    void refreshVerifyEmailCode(String email) throws Exception;
}