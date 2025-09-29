package project.domain.scheduleUser.dao;

import java.time.LocalDateTime;
import java.util.List;
import project.domain.scheduleUser.domain.ScheduleUser;

public interface ScheduleUserCustomRepository {

    List<ScheduleUser> findFetchByScheduleId(Long scheduleId);

    List<ScheduleUser> searchByUserAndDate(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
