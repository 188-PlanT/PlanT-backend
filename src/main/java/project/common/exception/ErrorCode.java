package project.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ErrorCode {

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
    WORKSPACE_ADMIN_NOT_EXIST(HttpStatus.NOT_FOUND, "워크스페이스 방장은 1명 이상이어야 합니다"),

    // schedule
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 스케줄입니다"),

    // chat
    CHAT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다"),

    // image
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 이미지입니다"),

    // date
    DATE_INVALID(HttpStatus.BAD_REQUEST, "잘못된 날짜 입력입니다"),

    // email
    EMAIL_CODE_INVALID(HttpStatus.NOT_FOUND, "인증번호가 올바르지 않습니다");

    private final HttpStatus status;
    private final String message;
}
