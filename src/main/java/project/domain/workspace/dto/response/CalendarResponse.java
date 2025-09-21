package project.domain.workspace.dto.response;

import static java.util.stream.Collectors.toList;

import java.util.List;
import project.domain.schedule.domain.Schedule;
import project.domain.schedule.dto.ScheduleDto;
import project.domain.workspace.domain.Workspace;

public record CalendarResponse(Long workspaceId, String workspaceName, List<ScheduleDto> schedules) {

    public static CalendarResponse of(Workspace workspace, List<Schedule> schedules) {
        List<ScheduleDto> scheduleDtos = schedules.stream().map(ScheduleDto::of).collect(toList());
        return new CalendarResponse(workspace.getId(), workspace.getName(), scheduleDtos);
    }
}
