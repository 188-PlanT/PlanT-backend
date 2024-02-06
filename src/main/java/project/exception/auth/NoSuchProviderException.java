package project.exception.auth;

public class NoSuchProviderException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "올바르지 않은 Provider입니다";
    
    public NoSuchProviderException() {
        super(DEFAULT_MESSAGE);
    }
    
    public NoSuchProviderException(String message) {
        super(message);
    }
}  