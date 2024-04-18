package project.domain.workspace.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;

import project.domain.user.domain.User;
import project.domain.user.domain.UserRole;
import project.domain.workspace.domain.UserWorkspace;
import project.domain.workspace.domain.Workspace;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
@NoArgsConstructor
public class FindWorkspaceUsersResponse{
    private Long workspaceId;
    private String workspaceName;
    private String profile;    
    
    private List<SimpleUserDto> users = new ArrayList<> ();
        
    public static FindWorkspaceUsersResponse from(Workspace workspace){
        FindWorkspaceUsersResponse dto = new FindWorkspaceUsersResponse();
        
        dto.setWorkspaceId(workspace.getId());
        dto.setWorkspaceName(workspace.getName());
        dto.setProfile(workspace.getProfile().getUrl());
        
        dto.setUsers(
            workspace.getUserWorkspaces().stream()
                .map(SimpleUserDto::new)
                .collect(toList())
        );
        
        return dto;
    }
    
    @Getter
     static class SimpleUserDto{
         private Long userId;
         private String nickName;
         private String email;
         private UserRole authority;
         
         public SimpleUserDto(UserWorkspace userWorkspace){
             User user = userWorkspace.getUser();
             
             this.userId = user.getId();
             this.nickName = user.getNickName();
             this.email = user.getEmail();
             this.authority = userWorkspace.getUserRole();
         }
     }   
}