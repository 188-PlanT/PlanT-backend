package project.dto.schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class DeleteScheduleUserResponse{
    private String message;
    
    public DeleteScheduleUserResponse(){
        this.message = "successfully delete user";
    }
}