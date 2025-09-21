package project.domain.workspaceUser.dto.request;

import jakarta.validation.constraints.NotNull;
import project.domain.workspaceUser.domain.WorkspaceUserRole;

public record WorkspaceUserUpdateRequest(@NotNull WorkspaceUserRole role) {}
