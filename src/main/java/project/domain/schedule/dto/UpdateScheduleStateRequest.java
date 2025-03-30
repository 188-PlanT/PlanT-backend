package project.domain.schedule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.domain.schedule.domain.Progress;

@Getter
@Setter
@NoArgsConstructor
public class UpdateScheduleStateRequest {
    @NotNull private Progress state;
}
