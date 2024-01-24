package project.dto.schedule;

import project.domain.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import static java.util.stream.Collectors.toList;
import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleDto{
    private Long scheduleId;
    private Long workspaceId;
    private String name;
    private List<UserDto> users = new ArrayList <> ();
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
    private Progress state;
    
    public static ScheduleDto from(Schedule schedule){
        ScheduleDto dto = new ScheduleDto();
        
        dto.setScheduleId(schedule.getId());
        dto.setWorkspaceId(schedule.getWorkspace().getId());
        dto.setName(schedule.getName());
        dto.setStartDate(schedule.getStartDate());
        dto.setEndDate(schedule.getEndDate());
        dto.setContent(schedule.getContent());
        dto.setState(schedule.getState());
        
        dto.setUsers(schedule.getUserSchedules().stream()
                        .map(us -> new UserDto(us.getUser()))
                        .collect(toList()));
        
        return dto;
    }
    
    
    
    @Getter
    static class UserDto{
        private Long userId;
        private String nickName;
        
        public UserDto(User user){
            this.userId = user.getId();
            this.nickName = user.getNickName();
        }
    }
    
        
    // public ScheduleDto(Schedule schedule){
    //     this.schedule_id = schedule.getId();
    //     this.workspace = schedule.getWorkspace().getName();
    //     this.name = schedule.getName();
            
    //     for (UserSchedule userSchedule : schedule.getUserSchedules()){
    //         users.add(userSchedule.getUser().getEmail());
    //     }
    // }
}