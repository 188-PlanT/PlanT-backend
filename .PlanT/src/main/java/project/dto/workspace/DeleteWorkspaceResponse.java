package project.dto.workspace;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class DeleteWorkspaceResponse{
    private String message;
    
    public DeleteWorkspaceResponse(){
        this.message = "successfully delete workspace";
    }
}