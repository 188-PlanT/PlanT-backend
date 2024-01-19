package project.exception.user;

import org.springframework.http.HttpStatus;

public class UnvalidAuthorityException extends RuntimeException {
    private HttpStatus status;
    
    public UnvalidAuthorityException() {
        super();
    }
    
    public UnvalidAuthorityException(String message){
        super(message);
    }
    
    public UnvalidAuthorityException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }
    
    public UnvalidAuthorityException(HttpStatus status){
        this.status = status;
    }
}