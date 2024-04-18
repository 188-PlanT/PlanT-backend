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
    
//    @ExceptionHandler(UserAlreadyExistException.class) // 중복 유저 예외
//    public ResponseEntity<ErrorResponse> ValidateErrorHandler(UserAlreadyExistException e){
//        log.info("UserAlreadyExistException");
//
//        ErrorResponse response = new ErrorResponse(ErrorCode.BAD_REQUEST, e.getMessage());
//
//        return ResponseEntity
//            .status(HttpStatus.BAD_REQUEST)
//            .body(response);
//    }
    
//    @ExceptionHandler(InvalidPasswordException.class) // 비밀번호 검증 실패 예외
//    public ResponseEntity<ErrorResponse> ValidateErrorHandler(InvalidPasswordException e){
//        log.info("InvalidPasswordException");
//
//        ErrorResponse response = new ErrorResponse(ErrorCode.BAD_REQUEST, e.getMessage());
//
//        return ResponseEntity
//            .status(HttpStatus.BAD_REQUEST)
//            .body(response);
//    }
    
//    @ExceptionHandler(DateFormatException.class) // 날짜 검증 실패 예외
//    public ResponseEntity<ErrorResponse> ValidateErrorHandler(DateFormatException e){
//        log.info("DateFormatException");
//
//        ErrorResponse response = new ErrorResponse(ErrorCode.BAD_REQUEST, e.getMessage());
//
//        return ResponseEntity
//            .status(HttpStatus.BAD_REQUEST)
//            .body(response);
//    }
    
    // <==401==>
//    @ExceptionHandler(UnIdentifiedUserException.class) // 토큰 인증 예외
//    public ResponseEntity<ErrorResponse> ValidateErrorHandler(UnIdentifiedUserException e){
//        log.info("UnIdentifiedUserException");
//
//        ErrorResponse response = new ErrorResponse(ErrorCode.UNAUTHORIZED, e.getMessage());
//
//        return ResponseEntity
//            .status(HttpStatus.UNAUTHORIZED)
//            .body(response);
//    }
//
//
//    // <== 403 ==>
//    @ExceptionHandler(InvalidAuthorityException.class) // 유저 권한 없음 예외
//    public ResponseEntity<ErrorResponse> ValidateErrorHandler(InvalidAuthorityException e){
//        log.info("InvalidAuthorityException");
//
//        ErrorResponse response = new ErrorResponse(ErrorCode.FORBIDDEN, e.getMessage());
//
//        return ResponseEntity
//            .status(HttpStatus.FORBIDDEN)
//            .body(response);
//    }
//
//
//    // <== 404 ==>
//    @ExceptionHandler(NoSuchUserException.class)
//    public ResponseEntity<ErrorResponse> invalidUserHandler(NoSuchUserException e){
//        log.info("NoSuchUserException");
//
//        ErrorResponse response = new ErrorResponse(ErrorCode.NOT_FOUND, e.getMessage());
//
//        return ResponseEntity
//            .status(HttpStatus.NOT_FOUND)
//            .body(response);
//    }
//
//    @ExceptionHandler(NoSuchWorkspaceException.class)
//    public ResponseEntity<ErrorResponse> invalidUserHandler(NoSuchWorkspaceException e){
//        log.info("NoSuchWorkspaceException");
//
//        ErrorResponse response = new ErrorResponse(ErrorCode.NOT_FOUND, e.getMessage());
//
//        return ResponseEntity
//            .status(HttpStatus.NOT_FOUND)
//            .body(response);
//    }
//
//    @ExceptionHandler(NoSuchScheduleException.class)
//    public ResponseEntity<ErrorResponse> invalidUserHandler(NoSuchScheduleException e){
//        log.info("NoSuchScheduleException");
//
//        ErrorResponse response = new ErrorResponse(ErrorCode.NOT_FOUND, e.getMessage());
//
//        return ResponseEntity
//            .status(HttpStatus.NOT_FOUND)
//            .body(response);
//    }
//
//    @ExceptionHandler(NoSuchChatException.class)
//    public ResponseEntity<ErrorResponse> invalidUserHandler(NoSuchChatException e){
//        log.info("NoSuchChatException");
//
//        ErrorResponse response = new ErrorResponse(ErrorCode.NOT_FOUND, e.getMessage());
//
//        return ResponseEntity
//            .status(HttpStatus.NOT_FOUND)
//            .body(response);
//    }
//
//    @ExceptionHandler(NoSuchProviderException.class)
//    public ResponseEntity<ErrorResponse> invalidUserHandler(NoSuchProviderException e){
//        log.info("NoSuchProviderException");
//
//        ErrorResponse response = new ErrorResponse(ErrorCode.NOT_FOUND, e.getMessage());
//
//        return ResponseEntity
//            .status(HttpStatus.NOT_FOUND)
//            .body(response);
//    }

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
    
//    @ExceptionHandler(InvalidTokenException.class)
//    public ResponseEntity<ErrorResponse> invalidUserHandler(InvalidTokenException e){
//        log.info("InvalidTokenException");
//
//        ErrorResponse response = new ErrorResponse(ErrorCode.NOT_FOUND, e.getMessage());
//
//        return ResponseEntity
//            .status(HttpStatus.NOT_FOUND)
//            .body(response);
//    }
//
//	@ExceptionHandler(NoWorkspaceAdminException.class)
//    public ResponseEntity<ErrorResponse> invalidUserHandler(NoWorkspaceAdminException e){
//        log.info("NoWorkspaceAdminException");
//
//        ErrorResponse response = new ErrorResponse(ErrorCode.NOT_FOUND, e.getMessage());
//
//        return ResponseEntity
//            .status(HttpStatus.NOT_FOUND)
//            .body(response);
//    }

    @ExceptionHandler(RuntimeException.class) // 500 ERROR
    public ResponseEntity<ErrorResponse> RuntimeErrorHandler(RuntimeException e){
        
        e.printStackTrace();
        
        ErrorResponse response = new ErrorResponse("INTERNAL_SERVER_ERROR", e.getMessage());
        
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }
}