package project.domain.schedule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import project.domain.schedule.domain.Progress;

public record ScheduleUpdateRequest(
        @NotBlank String name,
        @NotNull LocalDateTime startDate,
        @NotNull LocalDateTime endDate,
        @NotNull Progress state,
        String content) {}
