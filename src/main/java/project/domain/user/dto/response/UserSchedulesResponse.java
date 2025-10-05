package project.domain.user.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import project.domain.schedule.domain.Progress;
import project.domain.scheduleUser.domain.ScheduleUser;

public record UserSchedulesResponse(Long userId, ScheduleListDto schedules) {

    public static UserSchedulesResponse from(Long userId, List<ScheduleUser> scheduleUsers) {
        return new UserSchedulesResponse(userId, new ScheduleListDto(scheduleUsers));
    }

    public record ScheduleListDto(List<ScheduleDto> toDo, List<ScheduleDto> inProgress, List<ScheduleDto> done) {
        public ScheduleListDto(List<ScheduleUser> userSchedules) {
            this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            for (ScheduleUser us : userSchedules) {
                switch (us.getSchedule().getState().getKey()) {
                    case "TO_DO" -> toDo.add(new ScheduleDto(us));
                    case "IN_PROGRESS" -> inProgress.add(new ScheduleDto(us));
                    case "DONE" -> done.add(new ScheduleDto(us));
                }
            }
        }
    }

    public record ScheduleDto(
            Long scheduleId,
            Long workspaceId,
            String workspaceName,
            String scheduleName,
            LocalDateTime endDate,
            Progress state) {

        public ScheduleDto(ScheduleUser userSchedule) {
            this(
                    userSchedule.getSchedule().getId(),
                    userSchedule.getSchedule().getWorkspace().getId(),
                    userSchedule.getSchedule().getWorkspace().getName(),
                    userSchedule.getSchedule().getName(),
                    userSchedule.getSchedule().getEndDate(),
                    userSchedule.getSchedule().getState());
        }
    }
}
