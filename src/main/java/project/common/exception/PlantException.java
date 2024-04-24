package project.common.exception;

import lombok.Getter;

@Getter
public class PlantException extends RuntimeException{
    private final ErrorCode errorCode;

    // 기본 에러 양식
    public PlantException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // 조금 더 디테일한 메세지를 작성하고 싶을 때
    public PlantException(ErrorCode errorCode, String detail){
        super(detail);
        this.errorCode = errorCode;
    }
}
