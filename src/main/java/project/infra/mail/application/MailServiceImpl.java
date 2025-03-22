package project.infra.mail.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.infra.mail.dto.MailDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMail(MailDto dto) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(dto.recipient());
            mimeMessageHelper.setSubject(dto.subject());
            mimeMessageHelper.setText(dto.content(), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);
            log.info("Mail Success");
        } catch (MessagingException e) {
            log.info("Mail sending error");
            throw new PlantException(ErrorCode.MAIL_SEND_FAILED);
        }
    }
}
