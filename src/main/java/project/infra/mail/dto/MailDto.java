package project.infra.mail.dto;

public record MailDto(String recipient, String subject, String content) {
    public static MailDto from(String recipient, String subject, String content) {
        return new MailDto(recipient, subject, content);
    }
}
