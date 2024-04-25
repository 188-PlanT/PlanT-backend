package project.domain.schedule.dto;

import project.domain.schedule.domain.Progress;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateScheduleRequest{
    @NotNull
    private Long workspaceId;
    
    @NotBlank
    private String name;
    
    @NotNull
    private List<Long> users;

    @NotNull
    @JsonFormat(pattern = "yyyyMMdd:HH:mm")
    private LocalDateTime startDate;

    @NotNull
    @JsonFormat(pattern = "yyyyMMdd:HH:mm")
    private LocalDateTime endDate;

    @NotNull
    private Progress state;
    
    private String content;
}