package project.exception.schedule;

public class DateFormatException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "날짜 입력 양식 오류입니다";
    
    
    public DateFormatException() {
        super(DEFAULT_MESSAGE);
    }
    
    public DateFormatException(String message) {
        super(message);
    }
}  