package project.domain.schedule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ScheduleCreateRequest(
        @NotNull Long workspaceId,
        @NotBlank String name,
        @NotNull LocalDateTime startDate,
        @NotNull LocalDateTime endDate,
        String content) {}
