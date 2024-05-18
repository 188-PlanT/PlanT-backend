package project.domain.workspace.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class UpdateWorkspaceRequest{

    private String name;
    
    private String profile;
}