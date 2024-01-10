package project.dto.schedule;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateScheduleRequest{
    @NotBlank
    private String workspace;
    
    @NotBlank
    private String name;
    
    @NotBlank
    private LocalDateTime startDate;
    
    @NotBlank
    private LocalDateTime endDate;
    
    private String content;
    
    private List<String> users;
}