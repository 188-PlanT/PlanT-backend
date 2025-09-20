package project.domain.workspace.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddUserRequest(@NotNull @Positive Long userId) {}
