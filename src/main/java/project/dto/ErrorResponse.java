package project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import project.exception.ErrorCode;

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