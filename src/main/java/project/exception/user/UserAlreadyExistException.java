package project.exception.user;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends RuntimeException {
    private HttpStatus status;
    
    public UserAlreadyExistException() {
        super();
    }
    
    public UserAlreadyExistException(String message){
        super(message);
    }
    
    public UserAlreadyExistException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }
    
    public UserAlreadyExistException(HttpStatus status){
        this.status = status;
    }
}  