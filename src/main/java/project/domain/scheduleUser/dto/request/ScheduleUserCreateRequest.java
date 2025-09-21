package project.domain.scheduleUser.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ScheduleUserCreateRequest(
        @NotNull @Positive Long scheduleId,
        @NotNull @Positive Long userId
) {
}
