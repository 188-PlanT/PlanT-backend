package project.dto.user;

import project.domain.User;
import project.domain.Schedule;
import project.domain.Workspace;
import project.domain.Progress;

import lombok.Getter;
import lombok.AllArgsConstructor;
import java.util.List;
import java.time.LocalDateTime;
import static java.util.stream.Collectors.toList;

@Getter
@AllArgsConstructor
public class FindSingleUserResponse {
    private Long id;
    private String email;
    private String nickName;
    private String profile;
    private List<WorkspaceDto> workspaces;
    private List<ScheduleDto> schedules;
        
    public FindSingleUserResponse(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.profile = user.getProfile();
        this.nickName = user.getNickName();
            
        this.workspaces = user.getUserWorkspaces().stream()
            .map(userWorkspace -> new WorkspaceDto(userWorkspace.getWorkspace()))
            .collect(toList());
        
        this.schedules = user.getUserSchedules().stream()
            .map(userSchedule -> new ScheduleDto(userSchedule.getSchedule()))
            .collect(toList());
    }

    
    @Getter
    static class WorkspaceDto {
        private Long id;
        private String name;
        private String profile;

        public WorkspaceDto(Workspace workspace){
            this.id = workspace.getId();
            this.name = workspace.getName();
            this.profile = workspace.getProfile();
        }
    }
    
    @Getter
    static class ScheduleDto {
        private Long id;
        private String name;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Progress progress;

        public ScheduleDto(Schedule schedule){
            this.id = schedule.getId();
            this.name = schedule.getName();
            this.startDate = schedule.getStartDate();
            this.endDate = schedule.getEndDate();
            this.progress = schedule.getProgress();
        }
    }
}