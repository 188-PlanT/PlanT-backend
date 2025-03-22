package project.infra.mail.application;

import project.infra.mail.dto.MailDto;

public interface MailService {

    void sendMail(MailDto mailDto);
}
