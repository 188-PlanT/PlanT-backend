package project.exception.schedule;

import org.springframework.http.HttpStatus;

public class NoSuchScheduleException extends RuntimeException {
    private HttpStatus status;
    
    public NoSuchScheduleException() {
        super();
    }
    
    public NoSuchScheduleException(String message){
        super(message);
    }
    
    public NoSuchScheduleException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }
    
    public NoSuchScheduleException(HttpStatus status){
        this.status = status;
    }
}  