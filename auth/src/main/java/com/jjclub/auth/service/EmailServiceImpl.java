package com.jjclub.auth.service;

import com.jjclub.auth.dto.EmailValidateDto;
import com.jjclub.auth.domain.EmailValidationCode;
import com.jjclub.auth.repository.EmailValidateCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender emailSender;
    private final EmailValidateCodeRepository emailValidateCodeRepository;
    private final EmailValidateDto emailValidateDTO;

    /**
     * 이메일 인증 절차
     * 인증을 위한 이메일 포스트를 만듬
     *
     * @param email
     *        수신자 이메일
     */
    private MimeMessage createMessage(String email)throws Exception{
        String validationCode = createKey();
        log.info("보내는 대상 : "+ email);
        log.info("인증 번호 : " + validationCode);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, email); //보내는 대상
        message.setSubject("인증코드: " + validationCode); //제목

        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 JJ-Club 가입 창에 있는 코드 입력란에 적어주세요</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += validationCode;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html"); //내용
        message.setFrom(new InternetAddress("dlgkdis801@naver.com","JJ-Club")); //보내는 사람

        emailValidateCodeRepository.save(emailValidateDTO.toEntity(email, validationCode));

        return message;
    }

    /**
     * 이메일 인증을 위한 6자리 코드를 만듬
     *
     */
    @Transactional
    @Override
    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }

        return key.toString();
    }

    /**
     * 이메일 보내기
     *
     * @param email
     *  수신자 이메일
     * @throws MailException,
     * 이메일 에러
     */
    @Transactional
    @Override
    public void sendSimpleMessage(String email) throws Exception{
        MimeMessage message = createMessage(email);
        try{//예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    /**
     * 이메일 인증
     * @param email
     *      이메일인증할 사용자의 이메일
     * @param code
     *      인증을 할 인증 코드
     * @return
     *      인증한 코드가 맞는지 아닌지 boolean형으로 준다.
     */
    @Transactional
    @Override
    public boolean verifyEmailCode(String email, String code){
        EmailValidationCode emailValidationCode = emailValidateCodeRepository.findByEmail(email).get();
        String verifyCode = emailValidationCode.getCode();

        log.info("DB속 인증코드: " + verifyCode);
        log.info("입력 코드: " + code);
        if (verifyCode.equals(code)){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 인증코드 재발급
     * @param email
     *      재발급 받을 이메일
     */
    @Transactional
    @Override
    public void refreshVerifyEmailCode(String email) throws Exception {
        emailValidateCodeRepository.deleteByEmail(email);
        sendSimpleMessage(email);
    }
}
