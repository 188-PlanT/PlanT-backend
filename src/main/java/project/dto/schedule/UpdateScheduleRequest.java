package project.dto.schedule;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import project.domain.Schedule;
import static java.util.stream.Collectors.toList;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateScheduleRequest{
    @NotBlank
    private String name;
    
    private List<String> users;
    
    public UpdateScheduleRequest(Schedule schedule){
        this.name = schedule.getName();
        
        List<String> accountIdList = schedule.getUserSchedules().stream()
            .map(us -> us.getUser().getEmail())
            .collect(toList());
            
        this.users = accountIdList;
    }
}