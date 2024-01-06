package project.exception.workspace;

import org.springframework.http.HttpStatus;

public class NoSuchWorkspaceException extends RuntimeException {
    private HttpStatus status;
    
    public NoSuchWorkspaceException() {
        super();
    }
    
    public NoSuchWorkspaceException(String message){
        super(message);
    }
    
    public NoSuchWorkspaceException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }
    
    public NoSuchWorkspaceException(HttpStatus status){
        this.status = status;
    }
}  