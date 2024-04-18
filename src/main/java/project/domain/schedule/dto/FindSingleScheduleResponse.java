package project.domain.schedule.dto;

import project.domain.schedule.domain.Progress;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import project.domain.schedule.domain.Schedule;


@Getter
@NoArgsConstructor
public class FindSingleScheduleResponse{
    private Long schedule_id;
    private String workspace;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
    private Progress state;
    
    // private List<String> users = new ArrayList <> ();
    // private List<SimpleDevLogDto> devLogs = new ArrayList<>();
        
    public FindSingleScheduleResponse(Schedule schedule){
        this.schedule_id = schedule.getId();
        this.workspace = schedule.getWorkspace().getName();
        this.name = schedule.getName();
        this.startDate = schedule.getStartDate();
        this.endDate = schedule.getEndDate();
        this.content = schedule.getContent();
        this.state = schedule.getState();
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