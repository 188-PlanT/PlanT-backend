package project.dto.workspace;

import project.domain.*;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import static java.util.stream.Collectors.toList;
import com.fasterxml.jackson.annotation.JsonFormat;

@Getter @Setter
@NoArgsConstructor
public class CalendarResponse {
    private Long workspaceId;
    private String workspaceName;
	private UserRole role;
    
    List<ScheduleDto> schedules = new ArrayList<> ();
    
    public static CalendarResponse of(Workspace workspace, List<Schedule> schedules, Long loginUserId){
        CalendarResponse dto = new CalendarResponse();
        
        dto.setWorkspaceId(workspace.getId());
        dto.setWorkspaceName(workspace.getName());
        
        dto.setSchedules(
            schedules.stream()
            .map(ScheduleDto::new)
            .collect(toList())
        );
		
		for(UserWorkspace uw : workspace.getUserWorkspaces()){
			if (uw.getUser().getId() == loginUserId){
				dto.setRole(uw.getUserRole());
				break;
			}
		}
        
        return dto;
    }
    
    // test를 위해 public으로 선언
    @Getter
    public static class ScheduleDto{
        private Long scheduleId;
        private String scheduleName;
        @JsonFormat(pattern = "yyyyMMdd")
        private LocalDateTime startDate;
        @JsonFormat(pattern = "yyyyMMdd")
        private LocalDateTime endDate;
        private Progress state;
        
        public ScheduleDto(Schedule schedule){
            this.scheduleId = schedule.getId();
            this.scheduleName = schedule.getName();
            this.startDate = schedule.getStartDate();
            this.endDate = schedule.getEndDate();
            this.state = schedule.getState();
        }
    }
}