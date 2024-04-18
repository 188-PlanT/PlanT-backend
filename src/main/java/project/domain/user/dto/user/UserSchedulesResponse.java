package project.domain.user.dto.user;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import project.domain.schedule.domain.Progress;
import project.domain.schedule.domain.Schedule;
import project.domain.schedule.domain.UserSchedule;
import project.domain.user.domain.User;

@Setter
@Getter
@NoArgsConstructor
public class UserSchedulesResponse {
    private Long userId;
	private ScheduleListDto schedules;
    //private List<ScheduleDto> schedules = new ArrayList<> ();
    
    public static UserSchedulesResponse of(User user, List<UserSchedule> userSchedules){
        UserSchedulesResponse response = new UserSchedulesResponse();
        
        response.setUserId(user.getId());
        response.setSchedules(new ScheduleListDto(userSchedules));
        return response;
    }
	
	@Getter
	static class ScheduleListDto{
		private List<ScheduleDto> toDo = new ArrayList<> ();
		private List<ScheduleDto> inProgress = new ArrayList<> ();
		private List<ScheduleDto> done = new ArrayList<> ();
		
		public ScheduleListDto(List<UserSchedule> userSchedules){
			for (UserSchedule us : userSchedules){
				switch(us.getSchedule().getState().getKey()){
					case "TO_DO":
						toDo.add(new ScheduleDto(us));
						break;
					
					case "IN_PROGRESS":
						inProgress.add(new ScheduleDto(us));
						break;
					
					case "DONE":
						done.add(new ScheduleDto(us));
						break;
				}
			}
        }
	}
    
    @Getter
    static class ScheduleDto{
        private Long scheduleId;
		private Long workspaceId;
        private String workspaceName;
        private String scheduleName;
        @JsonFormat(pattern = "yyyyMMdd")
        private LocalDateTime endDate;
        private Progress state;
        
        public ScheduleDto(UserSchedule userSchedule){
			Schedule schedule = userSchedule.getSchedule();
			
            this.scheduleId = schedule.getId();
			this.workspaceId = schedule.getWorkspace().getId();
            this.workspaceName = schedule.getWorkspace().getName();
            this.scheduleName = schedule.getName();
            this.endDate = schedule.getEndDate();
            this.state = schedule.getState();
        }
    }
}