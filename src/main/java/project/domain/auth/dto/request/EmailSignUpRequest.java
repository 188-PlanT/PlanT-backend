package project.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// TODO: User로 패키지 위치 이동 검토
public record EmailSignUpRequest(@Email @NotBlank String email, @NotBlank String password) {}
