package project.exception.user;

public class InvalidPasswordException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "비밀번호는 8자~16자 사이의 영어 대문자, 소문자, 특수기호, 숫자만 가능합니다";
    
    public InvalidPasswordException() {
        super(DEFAULT_MESSAGE);
    }
    
    public InvalidPasswordException(String message) {
        super(message);
    }
}