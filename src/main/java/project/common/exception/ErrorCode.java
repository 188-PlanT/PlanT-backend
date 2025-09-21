package project.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // global
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 처리할 수 없는 문제가 발생했습니다. 관리자에게 문의 바랍니다"),

    // auth
    PASSWORD_INVALD(HttpStatus.BAD_REQUEST, "올바르지 않은 비밀번호 양식입니다"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "올바르지 않은 토큰입니다"),
    OAUTH_PROVIDER_NOT_FOUND(HttpStatus.NOT_FOUND, "잘못된 Provider입니다"),

    // user
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 유저입니다"),
    USER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 요청입니다"),
    USER_AUTHORITY_INVALID(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다"),

    // worksapce
    WORKSPACE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 워크스페이스입니다"),

    // workspaceUser
    WORKSPACE_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 워크스페이스 유저입니다"),
    WORKSPACE_USER_AUTHORITY_INVALID(HttpStatus.FORBIDDEN, "워크스페이스 유저 접근 권한이 없습니다"),
    WORKSPACE_USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 워크스페이스에 속한 유저입니다"),
    WORKSPACE_ADMIN_NOT_EXIST(HttpStatus.NOT_FOUND, "워크스페이스 방장은 1명 이상이어야 합니다"),

    // schedule
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 스케줄입니다"),

    // scheduleUser
    SCHEDULE_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 스케줄 유저입니다"),

    // chat
    CHAT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다"),
    CHAT_USER_NOT_IN_SCHEDULE(HttpStatus.CONFLICT, "채팅 작성자가 스케줄에 속해있지 않습니다"),
    CHAT_NOT_WRITER(HttpStatus.CONFLICT, "채팅 작성자가 아닙니다"),

    // image
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 이미지입니다"),

    // date
    DATE_INVALID(HttpStatus.BAD_REQUEST, "잘못된 날짜 입력입니다"),

    // email
    EMAIL_CODE_INVALID(HttpStatus.NOT_FOUND, "인증번호가 올바르지 않습니다"),
    MAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "메일 전송에 실패했습니다. 다시 시도해주세요."),
    ;

    private final HttpStatus status;
    private final String message;
}
