package project.domain.workspace.dto.response;

import static java.util.stream.Collectors.toList;

import java.util.List;
import project.domain.user.domain.UserRole;
import project.domain.workspace.domain.UserWorkspace;
import project.domain.workspace.domain.Workspace;

public record FindWorkspaceUsersResponse(
        Long workspaceId, String workspaceName, String profile, List<SimpleUserDto> users) {
    public static FindWorkspaceUsersResponse from(Workspace workspace) {
        List<SimpleUserDto> userDtos =
                workspace.getUserWorkspaces().stream().map(SimpleUserDto::new).collect(toList());

        return new FindWorkspaceUsersResponse(
                workspace.getId(), workspace.getName(), workspace.getProfile().getUrl(), userDtos);
    }

    public record SimpleUserDto(Long userId, String nickName, String email, UserRole authority) {
        public SimpleUserDto(UserWorkspace userWorkspace) {
            this(
                    userWorkspace.getUser().getId(),
                    userWorkspace.getUser().getNickName(),
                    userWorkspace.getUser().getEmail(),
                    userWorkspace.getUserRole());
        }
    }
}
