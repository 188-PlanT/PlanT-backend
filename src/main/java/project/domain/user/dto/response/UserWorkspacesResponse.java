package project.domain.user.dto.response;

import static java.util.stream.Collectors.toList;

import java.util.List;
import project.domain.workspaceUser.domain.WorkspaceUser;
import project.domain.workspaceUser.dto.WorkspaceUserDto;

public record UserWorkspacesResponse(Long userId, List<WorkspaceUserDto> workspaceUsers) {

    public static UserWorkspacesResponse from(Long userId, List<WorkspaceUser> workspaceUsers) {
        List<WorkspaceUserDto> workspaceDtos =
                workspaceUsers.stream().map(WorkspaceUserDto::of).collect(toList());
        return new UserWorkspacesResponse(userId, workspaceDtos);
    }
}
