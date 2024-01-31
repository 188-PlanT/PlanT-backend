package project.exception.devLog;

public class NoSuchChatException extends RuntimeException {
    private static final String DEFAULT_ERROR_MESSAGE = "댓글이 존재하지 않습니다";
    
    public NoSuchChatException() {
        super(DEFAULT_ERROR_MESSAGE);
    }
    
    public NoSuchChatException(String message){
        super(message);
    }
} 