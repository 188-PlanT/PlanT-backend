package project.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank String currentPassword, String newPassword, String nickName, String profile) {}
