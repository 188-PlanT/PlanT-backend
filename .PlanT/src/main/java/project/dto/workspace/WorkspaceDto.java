package project.dto.workspace;

import project.domain.Workspace;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class WorkspaceDto{
    private Long id;
    private String name;
    private List<String> users = new ArrayList<> ();
        
    // public WorkspaceDto(Workspace workspace){
    //     this.id = workspace.getId();
    //     this.name = workspace.getName();
        
    //     workspace.getUserWorkspaces().stream()
    //         .forEach(uw -> users.add(uw.getUser().getEmail()));
    // }
}