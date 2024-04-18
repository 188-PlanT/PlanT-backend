package project.domain.workspace.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class UpdateWorkspaceRequest{
    @NotBlank
    private String name;
    
    private String profile;
}