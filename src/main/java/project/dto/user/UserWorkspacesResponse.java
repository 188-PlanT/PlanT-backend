package project.dto.user;

import project.domain.*;

import java.util.List;
import java.util.ArrayList;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import static java.util.stream.Collectors.toList;

@Setter
@Getter
@NoArgsConstructor
public class UserWorkspacesResponse {
    private Long userId;
    private List<WorkspaceDto> workspaces = new ArrayList<> ();
    
    public static UserWorkspacesResponse of(User user, List<UserWorkspace> userWorkspaces){
        UserWorkspacesResponse response = new UserWorkspacesResponse();
        
        response.setUserId(user.getId());
        response.setWorkspaces(userWorkspaces.stream()
                                            .map(WorkspaceDto::new)
                                            .collect(toList()));
        
        return response;
    }
    
    @Getter
    static class WorkspaceDto{
        private Long workspaceId;
        private String workspaceName;
        private String profile;
		private UserRole role;
        
        public WorkspaceDto(UserWorkspace userWorkspace){
            this.workspaceId = userWorkspace.getWorkspace().getId();
            this.workspaceName = userWorkspace.getWorkspace().getName();
            this.profile = userWorkspace.getWorkspace().getProfile().getUrl();
			this.role = userWorkspace.getUserRole();
        }
    }
}