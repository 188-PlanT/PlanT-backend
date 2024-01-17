package project.dto.workspace;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class RemoveUserResponse{
    private String message;
    
    public RemoveUserResponse(){
        this.message = "successfully delete user";
    }
}