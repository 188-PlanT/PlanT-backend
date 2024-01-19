package project.exception.user;

public class UserAlreadyExistException extends RuntimeException {
    
    private static final String DEFAULT_MESSAGE = "이미 존재하는 유저입니다";
    
    public UserAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }
    
    public UserAlreadyExistException(String message){
        super(message);
    }
}  