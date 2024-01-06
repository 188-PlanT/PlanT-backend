package project.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class DeleteUserResponse{
    private String message;
    
    public DeleteUserResponse(){
        this.message = "successfully delete User";
    }
}