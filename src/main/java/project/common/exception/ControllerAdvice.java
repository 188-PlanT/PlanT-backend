package project.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice{

    @ExceptionHandler(PlantException.class)
    public ResponseEntity<ErrorResponse> PlantExceptionHandler(PlantException e){
        ErrorCode errorCode = e.getErrorCode();
        log.info("PlantException : {}", errorCode.name());

        ErrorResponse response = new ErrorResponse(errorCode.name(), e.getMessage());

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // spring Valid Exception
    public ResponseEntity<ErrorResponse> ValidateErrorHandler(MethodArgumentNotValidException e){
        log.info("MethodArgumentNotValidException");
        
        ErrorResponse response = new ErrorResponse("REQUEST_INVALID", e.getMessage());
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }

    @ExceptionHandler(NoHandlerFoundException.class) // handler를 찾을 수 없을 때 발생하는 오류
    public ResponseEntity<ErrorResponse> invalidUserHandler(NoHandlerFoundException e){
        log.info("NoHandlerFoundException");

        ErrorResponse response = new ErrorResponse("URL_INVALID", e.getMessage());
        
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response);
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class) // 메서드 오류
    public ResponseEntity<ErrorResponse> invalidUserHandler(HttpRequestMethodNotSupportedException e){
        log.info("HttpRequestMethodNotSupportedException");
        
        ErrorResponse response = new ErrorResponse("HTTP_METHOD_INVALID", e.getMessage());
        
        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(response);
    }

    @ExceptionHandler(RuntimeException.class) // 500 ERROR
    public ResponseEntity<ErrorResponse> RuntimeErrorHandler(RuntimeException e){
        
        e.printStackTrace();
        
        ErrorResponse response = new ErrorResponse("INTERNAL_SERVER_ERROR", e.getMessage());
        
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }
}