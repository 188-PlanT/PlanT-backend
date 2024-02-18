package project.dto.schedule;

import project.domain.Progress;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateScheduleRequest{
    @NotBlank
    private Long workspaceId;
    
    @NotBlank
    private String name;
    
    @NotBlank
    private List<Long> users;
    
    @NotBlank
    @JsonFormat(pattern = "yyyyMMdd:HH:mm")
    private LocalDateTime startDate;
    
    @NotBlank
    @JsonFormat(pattern = "yyyyMMdd:HH:mm")
    private LocalDateTime endDate;
    
    @NotBlank
    private Progress state;
    
    private String content;
}