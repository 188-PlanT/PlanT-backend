package project.dto.workspace;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.ArrayList;
import static java.util.stream.Collectors.toList;
import project.domain.Workspace;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class CreateWorkspaceRequest{
    @NotBlank
    private String name;
    
    private List<String> users = new ArrayList<> ();
    
    public CreateWorkspaceRequest(){
        this.name = null;
    }
    
    public CreateWorkspaceRequest(Workspace workspace){
        this.name = workspace.getName();
        
        List<String> userEmailList = workspace.getUserWorkspaces().stream()
            .map(uw -> uw.getUser().getEmail())
            .collect(toList());
        
        this.users.addAll(userEmailList);
    }
}