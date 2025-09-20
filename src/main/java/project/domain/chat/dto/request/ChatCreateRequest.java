package project.domain.chat.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ChatCreateRequest(@NotNull @Positive Long scheduleId, String content) {}
