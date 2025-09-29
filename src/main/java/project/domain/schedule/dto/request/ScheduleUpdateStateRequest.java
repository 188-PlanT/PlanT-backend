package project.domain.schedule.dto.request;

import jakarta.validation.constraints.NotNull;
import project.domain.schedule.domain.Progress;

public record ScheduleUpdateStateRequest(@NotNull Progress state) {}
