package project.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import project.domain.user.domain.User;

public record UpdateUserRequest(@NotBlank String currentPassword, String newPassword, String nickName, String profile) {
    public UpdateUserRequest(User user) {
        this(null, null, user.getNickName(), user.getProfile().getUrl());
    }
}
