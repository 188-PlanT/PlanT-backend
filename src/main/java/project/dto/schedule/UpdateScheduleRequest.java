package project.dto.schedule;

import project.domain.Schedule;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import java.time.LocalDateTime;
import static java.util.stream.Collectors.toList;
import javax.validation.constraints.NotBlank;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateScheduleRequest{
    @NotBlank
    private String name;
    
    @NotBlank
    private LocalDateTime startDate;
    
    @NotBlank
    private LocalDateTime endDate;
    
    private String content;
    
    private List<String> users;
    
    // public UpdateScheduleRequest(Schedule schedule){
    //     this.name = schedule.getName();
        
    //     this.startDate = schedule.startDate();
    // }
}