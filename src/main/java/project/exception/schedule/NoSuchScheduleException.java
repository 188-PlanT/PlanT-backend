package project.exception.schedule;

import org.springframework.http.HttpStatus;

public class NoSuchScheduleException extends RuntimeException {
    private static final String DEFAULT_ERROR_MESSAGE = "스케줄이 존재하지 않습니다";
    
    public NoSuchScheduleException() {
        super(DEFAULT_ERROR_MESSAGE);
    }
    
    public NoSuchScheduleException(String message){
        super(message);
    }
}  