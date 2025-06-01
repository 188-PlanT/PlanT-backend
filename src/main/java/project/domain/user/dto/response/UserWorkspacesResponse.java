package project.domain.user.dto.response;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.domain.user.domain.User;
import project.domain.user.domain.UserRole;
import project.domain.workspace.domain.UserWorkspace;

@Setter
@Getter
@NoArgsConstructor
public class UserWorkspacesResponse {
    private Long userId;
    private List<WorkspaceDto> workspaces = new ArrayList<>();

    public static UserWorkspacesResponse of(User user, List<UserWorkspace> userWorkspaces) {
        UserWorkspacesResponse response = new UserWorkspacesResponse();

        response.setUserId(user.getId());
        response.setWorkspaces(userWorkspaces.stream().map(WorkspaceDto::new).collect(toList()));

        return response;
    }

    @Getter
    static class WorkspaceDto {
        private Long workspaceId;
        private String workspaceName;
        private String profile;
        private UserRole role;

        public WorkspaceDto(UserWorkspace userWorkspace) {
            this.workspaceId = userWorkspace.getWorkspace().getId();
            this.workspaceName = userWorkspace.getWorkspace().getName();
            this.profile = userWorkspace.getWorkspace().getProfile().getUrl();
            this.role = userWorkspace.getUserRole();
        }
    }
}
