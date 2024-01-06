package project.exception.security;

import org.springframework.http.HttpStatus;

public class UnvalidTokenException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "올바르지 않은 토큰입니다";
    
    public UnvalidTokenException() {
        super(DEFAULT_MESSAGE);
    }
    
    public UnvalidTokenException(String message) {
        super(message);
    }
}  