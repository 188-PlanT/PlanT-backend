package project.service;

import project.domain.*;
import project.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.MessagingException;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService{
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final RedisService redisService;
    
    private final Long CODE_EXP_TIME = 1000L * 60 * 3; //3분
    
    
    public void sendValidateMail(String email, int code){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject("[PlanT] 회원가입 인증 코드입니다"); // 메일 제목
            mimeMessageHelper.setText("인증 코드는 " + code + "입니다.", true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            log.info("Mail Success");

        } catch (MessagingException e) {
            log.info("fail");
            throw new RuntimeException(e);
        }
        
        redisService.setValues(email, code + "");
        redisService.setExpiration(email, CODE_EXP_TIME);
    }
}