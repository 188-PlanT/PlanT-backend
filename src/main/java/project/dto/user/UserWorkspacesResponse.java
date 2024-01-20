package project.dto.user;

import project.domain.User;
import project.domain.Workspace;

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
    private int totalPages;
    private int pageNumber;
    private List<WorkspaceDto> workspaces = new ArrayList<> ();
    
    public static UserWorkspacesResponse of(User user, int totalPage, int pageNumber, List<Workspace> workspaces){
        UserWorkspacesResponse response = new UserWorkspacesResponse();
        
        response.setUserId(user.getId());
        response.setTotalPages(totalPage - 1);
        response.setPageNumber(pageNumber);
        
        response.setWorkspaces(workspaces.stream()
                                            .map(WorkspaceDto::new)
                                            .collect(toList()));
        
        return response;
    }
    
    @Getter
    static class WorkspaceDto{
        private Long workspaceId;
        private String workspaceName;
        private String profile;
        
        public WorkspaceDto(Workspace workspace){
            this.workspaceId = workspace.getId();
            this.workspaceName = workspace.getName();
            this.profile = workspace.getProfile();
        }
    }
}