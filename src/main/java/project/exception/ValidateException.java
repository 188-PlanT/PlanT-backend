package project.exception;

import org.springframework.http.HttpStatus;

public class ValidateException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "입력 양식 오류입니다";
    
    
    public ValidateException() {
        super(DEFAULT_MESSAGE);
    }
    
    public ValidateException(String message) {
        super(message);
    }
}  