package project.domain.workspace.dto.request;

import jakarta.validation.constraints.NotNull;
import project.domain.user.domain.UserRole;

public record UpdateUserRequest(@NotNull UserRole authority) {}
