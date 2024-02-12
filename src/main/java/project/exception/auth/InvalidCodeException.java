package project.exception.auth;

public class InvalidCodeException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "올바르지 않은 코드입니다";
    
    public InvalidCodeException() {
        super(DEFAULT_MESSAGE);
    }
    
    public InvalidCodeException(String message) {
        super(message);
    }
}  