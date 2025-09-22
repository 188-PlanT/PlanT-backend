package project.domain.workspace.dto.request;

import jakarta.validation.constraints.NotBlank;

public record WorkspaceUpdateRequest(@NotBlank String name, String profileUrl) {}
