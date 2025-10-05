package project.domain.schedule.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record ScheduleSearchByWorkspaceRequest(
        @NotNull @Positive Long workspaceId, @NotNull LocalDateTime startDate, @NotNull LocalDateTime endDate) {}
