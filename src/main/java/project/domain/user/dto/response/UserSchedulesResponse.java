package project.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import project.domain.schedule.domain.Progress;
import project.domain.schedule.domain.UserSchedule;
import project.domain.user.domain.User;

public record UserSchedulesResponse(Long userId, ScheduleListDto schedules) {

    public static UserSchedulesResponse of(User user, List<UserSchedule> userSchedules) {
        return new UserSchedulesResponse(user.getId(), new ScheduleListDto(userSchedules));
    }

    public record ScheduleListDto(List<ScheduleDto> toDo, List<ScheduleDto> inProgress, List<ScheduleDto> done) {
        public ScheduleListDto(List<UserSchedule> userSchedules) {
            this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            for (UserSchedule us : userSchedules) {
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
            @JsonFormat(pattern = "yyyyMMdd") LocalDateTime endDate,
            Progress state) {

        public ScheduleDto(UserSchedule userSchedule) {
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
