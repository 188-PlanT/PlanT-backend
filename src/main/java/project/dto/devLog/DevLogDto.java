package project.dto.devLog;

import lombok.Getter;
import lombok.AllArgsConstructor;
import project.domain.DevLog;

@Getter
@AllArgsConstructor
public class DevLogDto{
    private Long devLogId;
    private Long scheduleId;
    private String userEmail;
    private String content;
    
    public DevLogDto (DevLog devLog){
        this.devLogId = devLog.getId();
        this.scheduleId = devLog.getSchedule().getId();
        this.userEmail = devLog.getUser().getEmail();
        this.content = devLog.getContent();
    }
}

