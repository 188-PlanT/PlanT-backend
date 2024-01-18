package project.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

import project.exception.user.*;
import project.exception.ValidateException;
import project.exception.auth.UnIdentifiedUserException;
import project.dto.ErrorResponse;

import java.util.Map;
import java.util.HashMap;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice{
    
    // @ExceptionHandler(NoSuchUserException.class)
    // public ResponseEntity<ErrorResponse> invalidUserHandler(NoSuchUserException e){
    //     log.info("NoSuchUserException");
        
    //     ErrorResponse response = new ErrorResponse("404", e.getMessage());
        
    //     return ResponseEntity
    //         .status(HttpStatus.NOT_FOUND)
    //         .body(response);
    // }
    
    // @ExceptionHandler(UnIdentifiedUserException.class)
    // public ResponseEntity<ErrorResponse> UnAuthUserHandler(UnIdentifiedUserException e){
        
    //     ErrorResponse response = new ErrorResponse("401", e.getMessage());
        
    //     return ResponseEntity
    //         .status(HttpStatus.UNAUTHORIZED)
    //         .body(response);
    // }
    
    // @ExceptionHandler(ValidateException.class)
    // public ResponseEntity<ErrorResponse> ValidateErrorHandler(ValidateException e){
        
    //     ErrorResponse response = new ErrorResponse("404", e.getMessage());
        
    //     return ResponseEntity
    //         .status(HttpStatus.NOT_FOUND)
    //         .body(response);
    // }
    
    // @ExceptionHandler(MethodArgumentNotValidException.class) // spring Valid
    // public ResponseEntity<ErrorResponse> ValidateErrorHandler(MethodArgumentNotValidException e){
    //     //에러가 여러개면 첫번째 에러만 반환하도록
    //     String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        
    //     ErrorResponse response = new ErrorResponse("404", message);
        
    //     return ResponseEntity
    //         .status(HttpStatus.NOT_FOUND)
    //         .body(response);
    // }

    @ExceptionHandler(RuntimeException.class) // 500 ERROR
    public ResponseEntity<ErrorResponse> ValidateErrorHandler(RuntimeException e){
        //에러가 여러개면 첫번째 에러만 반환하도록
        
        String message = "서버에서 처리할 수 없는 에러가 발생했습니다";

        ErrorResponse response = new ErrorResponse("500", message);
        
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }
}