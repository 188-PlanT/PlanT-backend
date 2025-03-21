package project.infra.mail.application;

public interface EmailService {

    void sendValidateMail(String email, int code);
}
