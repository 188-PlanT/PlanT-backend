package project.domain.workspace.dto.request;

import jakarta.validation.constraints.NotBlank;

public record WorkspaceCreateRequest(@NotBlank String name) {}
