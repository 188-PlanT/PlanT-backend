package project.dto.schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;
import project.domain.*;
import static java.util.stream.Collectors.toList;

@Getter
@NoArgsConstructor
public class FindSingleScheduleResponse{
    private Long schedule_id;
    private String workspace;
    private String name;
    private List<String> users = new ArrayList <> ();
    // private List<SimpleDevLogDto> devLogs = new ArrayList<>();
        
    // public FindSingleScheduleResponse(Schedule schedule){
    //     this.schedule_id = schedule.getId();
    //     this.workspace = schedule.getWorkspace().getName();
    //     this.name = schedule.getName();
        
    //     for (UserSchedule userSchedule : schedule.getUserSchedules()){
    //         users.add(userSchedule.getUser().getEmail());
    //     }
        
    //     this.devLogs.addAll(
    //         schedule.getDevLogs().stream()
    //             .map(SimpleDevLogDto::new)
    //             .collect(toList())
    //     );
    // }
    
    // @Getter
    // static class SimpleDevLogDto{
    //     private Long devLogId;
    //     private String userEmail;
    //     private String content;
        
    //     public SimpleDevLogDto(DevLog devLog){
    //         this.devLogId = devLog.getId();
    //         this.userEmail = devLog.getUser().getEmail();
    //         this.content = devLog.getContent();
    //     }
    // }
}