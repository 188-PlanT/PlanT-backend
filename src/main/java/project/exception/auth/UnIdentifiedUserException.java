package project.exception.auth;

import org.springframework.http.HttpStatus;

public class UnIdentifiedUserException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "로그인이 필요한 서비스입니다";
    
    
    public UnIdentifiedUserException() {
        super(DEFAULT_MESSAGE);
    }
    
        public UnIdentifiedUserException(String message) {
        super(message);
    }
}  