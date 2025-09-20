package project.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record NickNameCheckRequest(@NotBlank String nickName) {}
