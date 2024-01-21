package project.dto.workspace;

import project.domain.Workspace;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;
import static java.util.stream.Collectors.toList;

@Getter
@Setter
@NoArgsConstructor
public class WorkspaceDto{
    private Long workspaceId;
    private String name;
    private String profile;
    private List<Long> users = new ArrayList<> ();
        
    public static WorkspaceDto from (Workspace workspace){
        WorkspaceDto dto = new WorkspaceDto();
        
        dto.setWorkspaceId(workspace.getId());
        dto.setName(workspace.getName());
        dto.setProfile(workspace.getProfile());
        
        List<Long> userIds = workspace.getUserWorkspaces().stream()
                                        .map(uw -> uw.getUser().getId())
                                        .collect(toList());
        
        dto.setUsers(userIds);
        
        return dto;
    }
}