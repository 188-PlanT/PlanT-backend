package project.dto.devLog;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class DeleteDevLogResponse{
    private String message;
    
    public DeleteDevLogResponse(){
        this.message = "successfully delete DevLog";
    }
}