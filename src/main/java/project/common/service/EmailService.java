package project.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import project.common.util.UrlUtil;

import static project.common.constant.MailContant.VALIDATE_EMAIL_CONTENT;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService{

    private final JavaMailSender javaMailSender;
    private final RedisServiceImpl redisService;
    private final UrlUtil urlUtil;
    private final Long CODE_EXP_TIME = 1000L * 60 * 5; //5분
	
	public void sendValidateMail(String email, int code){
		String subject = "[PLANT] 이메일 인증 메일입니다";
		sendMail(email, subject, getCodeEmailContent(code));

        redisService.setValues(email, code + "");
        redisService.setExpiration(email, CODE_EXP_TIME);
    }
    
	private void sendMail(String email, String subject, String content){
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		try{
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			mimeMessageHelper.setTo(email); // 메일 수신자
			mimeMessageHelper.setSubject(subject); // 메일 제목
			mimeMessageHelper.setText(content, true); // 메일 본문 내용, HTML 여부
			javaMailSender.send(mimeMessage);
			log.info("Mail Success");
		}
		catch (MessagingException e) {
            log.info("Mail sending error");
            throw new RuntimeException(e);
        }
	}
	
    private String getCodeEmailContent(int code){
        return String.format(VALIDATE_EMAIL_CONTENT, urlUtil.getApiUrl(), code);
    }
}