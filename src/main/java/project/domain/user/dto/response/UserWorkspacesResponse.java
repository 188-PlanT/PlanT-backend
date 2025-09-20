package project.domain.user.dto.response;

import static java.util.stream.Collectors.toList;

import java.util.List;
import project.domain.user.domain.User;
import project.domain.user.domain.UserRole;
import project.domain.workspace.domain.UserWorkspace;

public record UserWorkspacesResponse(Long userId, List<WorkspaceDto> workspaces) {

    public static UserWorkspacesResponse from(User user, List<UserWorkspace> userWorkspaces) {
        List<WorkspaceDto> workspaceDtos =
                userWorkspaces.stream().map(WorkspaceDto::of).collect(toList());
        return new UserWorkspacesResponse(user.getId(), workspaceDtos);
    }

    public record WorkspaceDto(Long workspaceId, String workspaceName, String profile, UserRole role) {

        public static WorkspaceDto of(UserWorkspace userWorkspace) {
            return new WorkspaceDto(
                    userWorkspace.getWorkspace().getId(),
                    userWorkspace.getWorkspace().getName(),
                    userWorkspace.getWorkspace().getProfile().getUrl(),
                    userWorkspace.getUserRole());
        }
    }
}
