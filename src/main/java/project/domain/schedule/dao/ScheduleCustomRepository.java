package project.domain.schedule.dao;

import java.time.LocalDateTime;
import java.util.List;
import project.domain.schedule.domain.Schedule;

public interface ScheduleCustomRepository {

    List<Schedule> searchByWorkspaceInAndDateBetween(
            List<Long> workspaceIds, LocalDateTime startDate, LocalDateTime endDate);
}
