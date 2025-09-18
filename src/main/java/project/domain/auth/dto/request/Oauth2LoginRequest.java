package project.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record Oauth2LoginRequest(@NotBlank String code, @NotBlank String provider) {}
