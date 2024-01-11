package project.dto.schedule;

import project.domain.Progress;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import project.domain.*;
import static java.util.stream.Collectors.toList;


@Getter
@NoArgsConstructor
public class FindSingleScheduleResponse{
    private Long schedule_id;
    private String workspace;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
    private Progress progress;
    
    // private List<String> users = new ArrayList <> ();
    // private List<SimpleDevLogDto> devLogs = new ArrayList<>();
        
    public FindSingleScheduleResponse(Schedule schedule){
        this.schedule_id = schedule.getId();
        this.workspace = schedule.getWorkspace().getName();
        this.name = schedule.getName();
        this.startDate = schedule.getStartDate();
        this.endDate = schedule.getEndDate();
        this.content = schedule.getContent();
        this.progress = schedule.getProgress();
    }
    
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