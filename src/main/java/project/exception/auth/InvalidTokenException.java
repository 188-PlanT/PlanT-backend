package project.exception.auth;

public class InvalidTokenException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "올바르지 않은 토큰입니다";
    
    public InvalidTokenException() {
        super(DEFAULT_MESSAGE);
    }
    
    public InvalidTokenException(String message) {
        super(message);
    }
}  