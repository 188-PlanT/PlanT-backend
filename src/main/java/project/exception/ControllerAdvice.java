package project.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;


import project.exception.user.*;
import project.exception.auth.*;
import project.exception.schedule.*;
import project.exception.auth.UnIdentifiedUserException;
import project.dto.ErrorResponse;
import project.dto.ErrorCode;

import java.util.Map;
import java.util.HashMap;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice{
    
    // <== 400 ==>
    @ExceptionHandler(MethodArgumentNotValidException.class) // spring Valid Exception
    public ResponseEntity<ErrorResponse> ValidateErrorHandler(MethodArgumentNotValidException e){
        log.info("MethodArgumentNotValidException");
        
        ErrorResponse response = new ErrorResponse(ErrorCode.BAD_REQUEST, e.getMessage());
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
    
    @ExceptionHandler(UserAlreadyExistException.class) // 중복 유저 예외
    public ResponseEntity<ErrorResponse> ValidateErrorHandler(UserAlreadyExistException e){
        log.info("UserAlreadyExistException");
        
        ErrorResponse response = new ErrorResponse(ErrorCode.BAD_REQUEST, e.getMessage());
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
    
    @ExceptionHandler(InvalidPasswordException.class) // 비밀번호 검증 실패 예외
    public ResponseEntity<ErrorResponse> ValidateErrorHandler(InvalidPasswordException e){
        log.info("InvalidPasswordException");
        
        ErrorResponse response = new ErrorResponse(ErrorCode.BAD_REQUEST, e.getMessage());
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
    
    @ExceptionHandler(DateFormatException.class) // 비밀번호 검증 실패 예외
    public ResponseEntity<ErrorResponse> ValidateErrorHandler(DateFormatException e){
        log.info("DateFormatException");
        
        ErrorResponse response = new ErrorResponse(ErrorCode.BAD_REQUEST, e.getMessage());
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
    
    // <==401==>
    @ExceptionHandler(UnIdentifiedUserException.class) // 토큰 인증 예외
    public ResponseEntity<ErrorResponse> ValidateErrorHandler(UnIdentifiedUserException e){
        log.info("UnIdentifiedUserException");
        
        ErrorResponse response = new ErrorResponse(ErrorCode.UNAUTHORIZED, e.getMessage());
        
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(response);
    }
    
    
    // <== 403 ==>
    @ExceptionHandler(InvalidAuthorityException.class) // 유저 권한 없음 예외
    public ResponseEntity<ErrorResponse> ValidateErrorHandler(InvalidAuthorityException e){
        log.info("InvalidAuthorityException");
        
        ErrorResponse response = new ErrorResponse(ErrorCode.FORBIDDEN, e.getMessage());
        
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(response);
    }
    
    
    // <== 404 ==>
    @ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity<ErrorResponse> invalidUserHandler(NoSuchUserException e){
        log.info("NoSuchUserException");
        
        ErrorResponse response = new ErrorResponse(ErrorCode.NOT_FOUND, e.getMessage());
        
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response);
    }
    
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> invalidUserHandler(NoHandlerFoundException e){
        log.info("url 오류");
        
        ErrorResponse response = new ErrorResponse(ErrorCode.NOT_FOUND, e.getMessage());
        
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response);
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> invalidUserHandler(HttpRequestMethodNotSupportedException e){
        log.info("Http Method 오류");
        
        ErrorResponse response = new ErrorResponse(ErrorCode.NOT_FOUND, e.getMessage());
        
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response);
    }
    
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> invalidUserHandler(InvalidTokenException e){
        log.info("InvalidTokenException");
        
        ErrorResponse response = new ErrorResponse(ErrorCode.NOT_FOUND, e.getMessage());
        
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response);
    }

    @ExceptionHandler(RuntimeException.class) // 500 ERROR
    public ResponseEntity<ErrorResponse> ValidateErrorHandler(RuntimeException e){
        //에러가 여러개면 첫번째 에러만 반환하도록
        
        e.printStackTrace();
        
        ErrorResponse response = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }
}