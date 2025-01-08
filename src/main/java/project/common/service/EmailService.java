package project.common.service;

import project.domain.user.dao.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Value;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService{
    private final JavaMailSender javaMailSender;
    private final RedisService redisService;
    
    private final Long CODE_EXP_TIME = 1000L * 60 * 5; //5분
    private static final String CODE_MAIL_MESSAGE = "<div style='font-family: IBM Plex Sans KR; width: 600px; height: 400px; border: 4px solid #134074; margin: 50px auto; box-sizing: border-box; border-radius: 8px;'><div style='display: flex; align-items: center'><a href ='%s'><img style='width: 34px; height: 50px; padding-left: 20px; padding-top: 20px; padding-right: 10px' src='https://plant-s3.s3.ap-northeast-2.amazonaws.com/logo.png' /></a><h1><span style='color: #134074; font-size: 36px; font-family: IBM Plex Sans KR; font-weight: 800; word-wrap: break-word'>PLAN,T 이메일 주소 인증</span> </h1></div><p style='line-height: 26px; margin-top: 10px; padding: 0 20px; color: black; font-size: 18px; font-family: IBM Plex Sans KR; font-weight: 500;'>서비스 이용을 위해 이메일 주소 인증을 요청하셨습니다.<br/> 아래 인증 번호를 입력하여 인증을 완료해주세요.</p><h1 style='margin: 0; padding: 0 20px;'><span style='color: #134074; font-size: 32px; font-family: IBM Plex Sans KR; font-weight: 800; word-wrap: break-word'>%d</span> </h1></div>";
	// private static final String INVITATION_MAIL_MESSAGE = "<div style='font-family: IBM Plex Sans KR; width: 700px; height: 400px; border: 4px solid #134074; margin: 50px auto; box-sizing: border-box; border-radius: 8px;'><div style='display: flex; align-items: center'><a href ='%s'><img style='width: 34px; height: 50px; padding-left: 20px; padding-top: 20px; padding-right: 10px' src='https://plant-s3.s3.ap-northeast-2.amazonaws.com/logo.png' /></a><h1><span style='color: #134074; font-size: 36px; font-family: IBM Plex Sans KR; font-weight: 800; word-wrap: break-word'>PLAN,T 워크스페이스에 초대합니다</span> </h1></div><p style='line-height: 26px; margin-top: 10px; padding: 0 20px; color: black; font-size: 18px; font-family: IBM Plex Sans KR; font-weight: 500;'>[%s] 워크스페이스에 초대합니다.<br/> 아래 링크를 클릭해 워크스페이스에 참여해보세요.</p><h1 style='margin: 0; padding: 0 20px; display: flex; justify-content: center;'><a href='%s' style='color: white; background-color: #134074; font-size: 32px; font-family: IBM Plex Sans KR; font-weight: 600; word-wrap: break-word; margin-top: 20px; padding: 10px; border-radius: 12px; text-decoration: none' >참여하러가기</a> </h1></div>";
	
	@Value("${front.main-url}")
	private String MAIN_URL;
	
	// @Value("${front.invite-url}")
	// private String INVITE_URL;
	
	public void sendValidateMail(String email, int code){
		String subject = "[PLANT] 이메일 인증 메일입니다";
		sendMail(email, subject, getCodeEmailContent(code));

        redisService.setValues(email, code + "");
        redisService.setExpiration(email, CODE_EXP_TIME);
    }
	
	// public void sendInvitationMail(String email, String workspaceName){
	// 	String subject = "[PLANT] 워크스페이스에 초대합니다";
	// 	sendMail(email, subject, getInvitationEmailContent(workspaceName));
	// }
    
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
        return String.format(CODE_MAIL_MESSAGE, MAIN_URL, code);
    }
	
	// private String getInvitationEmailContent(String workspaceName){
	// 	return String.format(INVITATION_MAIL_MESSAGE, MAIN_URL, workspaceName, INVITE_URL);
	// }
}