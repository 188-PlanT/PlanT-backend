package project.exception.image;

public class NoSuchImageException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "이미지를 찾을 수 없습니다";
    
    public NoSuchImageException() {
        super(DEFAULT_MESSAGE);
    }
    
    public NoSuchImageException(String message){
        super(message);
    }
}  