package project.exception.workspace;

import org.springframework.http.HttpStatus;

public class NoWorkspaceAdminException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "어드민은 1명 이상 존재해야합니다.";
    
    public NoWorkspaceAdminException() {
        super(DEFAULT_MESSAGE);
    }
    
    public NoWorkspaceAdminException(String message){
        super(message);
    }
}  