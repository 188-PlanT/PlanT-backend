package project.domain.workspaceUser.dto.response;

import java.util.List;
import project.domain.workspaceUser.domain.WorkspaceUser;
import project.domain.workspaceUser.dto.WorkspaceUserDto;

public record WorkspaceUsersResponse(List<WorkspaceUserDto> workspaceUsers) {

    public static WorkspaceUsersResponse of(List<WorkspaceUser> workspaceUsers) {
        return new WorkspaceUsersResponse(
                workspaceUsers.stream().map(WorkspaceUserDto::of).toList());
    }
}
