package project.domain.schedule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import project.domain.schedule.domain.Progress;

public record UpdateScheduleRequest(
        @NotBlank String name,
        @NotNull List<Long> users,
        @NotNull LocalDateTime startDate,
        @NotNull LocalDateTime endDate,
        @NotNull Progress state,
        String content) {}
