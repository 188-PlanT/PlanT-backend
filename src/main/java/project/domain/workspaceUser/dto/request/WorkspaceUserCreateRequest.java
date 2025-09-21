package project.domain.workspaceUser.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WorkspaceUserCreateRequest(@NotNull @Positive Long workspaceId, @NotNull @Positive Long userId) {}
