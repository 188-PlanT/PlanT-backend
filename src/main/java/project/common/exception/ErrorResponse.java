package project.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse{
    private final String errorName;
    private final String message;

//    public ErrorResponse(ErrorCode errorCode){
//        this.errorName = errorCode.name();
//        this.message = errorCode.getMessage();
//    }
}