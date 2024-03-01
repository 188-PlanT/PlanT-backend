package project.dto.user;

import project.domain.User;
import project.domain.Schedule;
import project.domain.Progress;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import static java.util.stream.Collectors.toList;
import com.fasterxml.jackson.annotation.JsonFormat;

@Setter
@Getter
@NoArgsConstructor
public class UserSchedulesResponse {
    private String email;
    private int totalPages;
    private int pageNumber;
    private List<ScheduleDto> schedules = new ArrayList<> ();
    
    public static UserSchedulesResponse of(String email, int totalPage, int pageNumber, List<Schedule> schedules){
        UserSchedulesResponse response = new UserSchedulesResponse();
        
        response.setEmail(email);
        response.setTotalPages(totalPage);
        response.setPageNumber(pageNumber + 1);
        
        response.setSchedules(schedules.stream()
                                            .map(ScheduleDto::new)
                                            .collect(toList()));
        return response;
    }
    
    @Getter
    static class ScheduleDto{
        private Long scheduleId;
        private String workspaceName;
        private String scheduleName;
        @JsonFormat(pattern = "yyyyMMdd")
        private LocalDateTime endDate;
        private Progress state;
        
        public ScheduleDto(Schedule schedule){
            this.scheduleId = schedule.getId();
            this.workspaceName = schedule.getWorkspace().getName();
            this.scheduleName = schedule.getName();
            this.endDate = schedule.getEndDate();
            this.state = schedule.getState();
        }
    }
}