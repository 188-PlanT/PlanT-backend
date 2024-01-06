package project.dto.schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;
import project.domain.*;

@Getter
@NoArgsConstructor
public class ScheduleDto{
    private Long schedule_id;
    private String workspace;
    private String name;
    private List<String> users = new ArrayList <> ();
        
    public ScheduleDto(Schedule schedule){
        this.schedule_id = schedule.getId();
        this.workspace = schedule.getWorkspace().getName();
        this.name = schedule.getName();
            
        for (UserSchedule userSchedule : schedule.getUserSchedules()){
            users.add(userSchedule.getUser().getEmail());
        }
    }
}