package project.domain.workspace.dto.response;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import project.domain.schedule.domain.Progress;
import project.domain.schedule.domain.Schedule;
import project.domain.user.domain.UserRole;
import project.domain.workspace.domain.UserWorkspace;
import project.domain.workspace.domain.Workspace;

public record CalendarResponse(Long workspaceId, String workspaceName, UserRole role, List<ScheduleDto> schedules) {

    public static CalendarResponse of(Workspace workspace, List<Schedule> schedules, Long loginUserId) {
        UserRole userRole = workspace.getUserWorkspaces().stream()
                .filter(uw -> uw.getUser().getId().equals(loginUserId))
                .findFirst()
                .map(UserWorkspace::getUserRole)
                .orElse(null); // Or handle appropriately

        List<ScheduleDto> scheduleDtos =
                schedules.stream().map(ScheduleDto::new).collect(toList());

        return new CalendarResponse(workspace.getId(), workspace.getName(), userRole, scheduleDtos);
    }

    public record ScheduleDto(
            Long scheduleId,
            String scheduleName,
            @JsonFormat(pattern = "yyyyMMdd") LocalDateTime startDate,
            @JsonFormat(pattern = "yyyyMMdd") LocalDateTime endDate,
            Progress state) {
        public ScheduleDto(Schedule schedule) {
            this(
                    schedule.getId(),
                    schedule.getName(),
                    schedule.getStartDate(),
                    schedule.getEndDate(),
                    schedule.getState());
        }
    }
}
