package project.domain.chat.domain;

import java.util.List;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.domain.schedule.domain.Schedule;
import project.domain.schedule.domain.UserSchedule;
import project.domain.user.domain.User;

public class ChatDomainService {

    public void validateWhenCreate(Schedule schedule, User user) {
        isUserInSchedule(schedule, user);
    }

    private void isUserInSchedule(Schedule schedule, User user) {
        List<User> scheduleUsers =
                schedule.getUserSchedules().stream().map(UserSchedule::getUser).toList();
        if (!scheduleUsers.contains(user)) {
            throw new PlantException(ErrorCode.CHAT_USER_NOT_IN_SCHEDULE);
        }
    }

    public void validateWhenUpdateOrDelete(Chat chat, User user) {
        if (!chat.getUser().equals(user)) {
            throw new PlantException(ErrorCode.CHAT_NOT_WRITER);
        }
    }
}
