package project.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;

public record CodeRequest(@NotNull String code) {}
