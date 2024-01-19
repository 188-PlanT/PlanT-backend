package project.dto;

import lombok.Getter;

@Getter
public class ErrorResponse{
    private int code;
    private String message;
    private String details;
    
    public ErrorResponse(ErrorCode errorCode, String details){
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.details = details;
    }
}