package project.exception.devLog;

import org.springframework.http.HttpStatus;

public class NoSuchDevLogException extends RuntimeException {
    private HttpStatus status;
    
    public NoSuchDevLogException() {
        super();
    }
    
    public NoSuchDevLogException(String message){
        super(message);
    }
    
    public NoSuchDevLogException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }
    
    public NoSuchDevLogException(HttpStatus status){
        this.status = status;
    }
} 