package project.exception.user;

public class InvalidAuthorityException extends RuntimeException {
    
    private static final String DEFAULT_MESSAGE = "권한이 올바르지 않습니다";
    
    public InvalidAuthorityException() {
        super(DEFAULT_MESSAGE);
    }
    
    public InvalidAuthorityException(String message){
        super(message);
    }
}