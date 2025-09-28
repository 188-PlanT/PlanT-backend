package project.domain.scheduleUser.dao;

import java.util.List;
import project.domain.scheduleUser.domain.ScheduleUser;

public interface ScheduleUserCustomRepository {

    List<ScheduleUser> findFetchByScheduleId(Long scheduleId);
}
