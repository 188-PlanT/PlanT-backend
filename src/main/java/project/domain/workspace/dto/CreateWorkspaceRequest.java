package project.domain.workspace.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.ArrayList;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class CreateWorkspaceRequest{
    @NotBlank
    private String name;
    
    private String profile;
    
    private List<Long> users = new ArrayList<> ();
}