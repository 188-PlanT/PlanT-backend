package project.exception.workspace;

import org.springframework.http.HttpStatus;

public class NoSuchWorkspaceException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "존재하지 않는 워크스페이스입니다";
    
    public NoSuchWorkspaceException() {
        super(DEFAULT_MESSAGE);
    }
    
    public NoSuchWorkspaceException(String message){
        super(message);
    }
}  