package project.domain.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.domain.schedule.domain.Progress;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateScheduleRequest {
    @NotBlank private String name;

    @NotNull private List<Long> users;

    @NotNull @JsonFormat(pattern = "yyyyMMdd:HH:mm")
    private LocalDateTime startDate;

    @NotNull @JsonFormat(pattern = "yyyyMMdd:HH:mm")
    private LocalDateTime endDate;

    @NotNull private Progress state;

    private String content;
}
