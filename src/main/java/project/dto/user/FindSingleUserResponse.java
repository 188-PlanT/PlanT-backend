package project.dto.user;

import lombok.Getter;
import lombok.AllArgsConstructor;
import java.util.List;
import project.domain.User;
import static java.util.stream.Collectors.toList;

@Getter
@AllArgsConstructor
public class FindSingleUserResponse {
    private String email;
    private String name;
    private List<String> workspaces;
    private List<String> schedules;
    // private List<Long> devLogs;
        
    public FindSingleUserResponse(User user){
        this.email = user.getEmail();
        this.name = user.getName();
            
        this.workspaces = user.getUserWorkspaces().stream()
            .map(userWorkspace -> userWorkspace.getWorkspace().getName())
            .collect(toList());
        
        this.schedules = user.getUserSchedules().stream()
            .map(userSchedule -> userSchedule.getSchedule().getName())
            .collect(toList());
    }
}