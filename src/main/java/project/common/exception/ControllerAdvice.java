package project.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice{

    @ExceptionHandler(PlantException.class) // custom Exception
    public ResponseEntity<ErrorResponse> PlantExceptionHandler(PlantException e){
        ErrorCode errorCode = e.getErrorCode();
        String message = e.getMessage();

        ErrorResponse response = new ErrorResponse(errorCode.name(), message);

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // spring Validation Exception
    public ResponseEntity<ErrorResponse> ValidateErrorHandler(MethodArgumentNotValidException e){
        log.info("MethodArgumentNotValidException");

//        Map<String, String> errors = new HashMap<>();
        List<String> errors = new ArrayList<>();

        e.getBindingResult().getAllErrors()
                .forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
//                    errors.put(fieldName, errorMessage)
                    errors.add(fieldName + " : " + errorMessage);
                });

        ErrorResponse response = new ErrorResponse("REQUEST_INVALID", errors.toString());
        
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
        
        // e.printStackTrace();
        log.error("", e);
		
        ErrorResponse response = new ErrorResponse("INTERNAL_SERVER_ERROR", e.getMessage());
        
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }
}