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
    
    public void sendMail(){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo("kimsh8580@gmail.com"); // 메일 수신자
            mimeMessageHelper.setSubject("test mail"); // 메일 제목
            mimeMessageHelper.setText("hello", true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            log.info("Success");

        } catch (MessagingException e) {
            log.info("fail");
            throw new RuntimeException(e);
        }
    }
}