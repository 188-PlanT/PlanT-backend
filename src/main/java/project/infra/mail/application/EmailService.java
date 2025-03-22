package project.infra.mail.application;

import project.infra.mail.dto.MailDto;

public interface EmailService {

    void sendValidateMail(String email, int code);

    void sendMail(MailDto mailDto);
}
