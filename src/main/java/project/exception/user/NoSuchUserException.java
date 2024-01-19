package project.exception.user;

public class NoSuchUserException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "유저를 찾을 수 없습니다";
    
    public NoSuchUserException() {
        super(DEFAULT_MESSAGE);
    }
    
    public NoSuchUserException(String message){
        super(message);
    }
}  