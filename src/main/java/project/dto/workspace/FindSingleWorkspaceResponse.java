package project.dto.workspace;

import lombok.Getter;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.ArrayList;
import project.domain.*;
import static java.util.stream.Collectors.toList;

@Getter
@AllArgsConstructor
public class FindSingleWorkspaceResponse{
    private Long id;
    private String name;
    private List<String> users = new ArrayList<> ();
    private List<ScheduleSimpleDto> schedules = new ArrayList<>(); 
        
    public FindSingleWorkspaceResponse(Workspace workspace){
        this.id = workspace.getId();
        this.name = workspace.getName();
        
        workspace.getUserWorkspaces().stream()
            .forEach(uw -> users.add(uw.getUser().getEmail()));
        
        schedules.addAll(
        workspace.getSchedules().stream()
            .map(ScheduleSimpleDto::new)
            .collect(toList())
        );
    }
    
    @Getter
     static class ScheduleSimpleDto{
         private Long scheduleId;
         private String scheduleName;
         
         public ScheduleSimpleDto(Schedule schedule){
             this.scheduleId = schedule.getId();
             this.scheduleName = schedule.getName();
         }
     }   
}