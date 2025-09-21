package project.domain.workspaceUser.dto;

import project.domain.workspaceUser.domain.WorkspaceUser;
import project.domain.workspaceUser.domain.WorkspaceUserRole;

public record WorkspaceUserDto(Long id, Long workspaceId, Long userId, WorkspaceUserRole role) {

    public static WorkspaceUserDto of(WorkspaceUser workspaceUser) {
        return new WorkspaceUserDto(
                workspaceUser.getId(),
                workspaceUser.getWorkspace().getId(),
                workspaceUser.getUser().getId(),
                workspaceUser.getRole());
    }
}
