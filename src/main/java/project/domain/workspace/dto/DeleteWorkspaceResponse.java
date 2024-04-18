package project.domain.workspace.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class DeleteWorkspaceResponse{
    private String message;
    
    public DeleteWorkspaceResponse(){
        this.message = "successfully delete workspace";
    }
}