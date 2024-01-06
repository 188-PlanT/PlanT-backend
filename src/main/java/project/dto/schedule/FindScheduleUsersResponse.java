package project.dto.schedule;

import lombok.Getter;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.ArrayList;
import project.domain.*;
import static java.util.stream.Collectors.toList;

@Getter
@AllArgsConstructor
public class FindScheduleUsersResponse{
    private Long scheduleId;
    private String scheduleName;
    
    private List<SimpleUserDto> users = new ArrayList<>();
    
    public FindScheduleUsersResponse(Schedule schedule){
        this.scheduleId = schedule.getId();
        this.scheduleName = schedule.getName();
        
        this.users.addAll(
            schedule.getUserSchedules().stream()
                .map(userSchedule -> new SimpleUserDto(userSchedule.getUser()))
                .collect(toList())
        );
    }
    
    @Getter
    static class SimpleUserDto{
        private Long userId;
        private String userEmail;
        private String userName;
        
        public SimpleUserDto(User user){
            this.userId = user.getId();
            this.userEmail = user.getEmail();
            this.userName = user.getName();
        }
    }
}