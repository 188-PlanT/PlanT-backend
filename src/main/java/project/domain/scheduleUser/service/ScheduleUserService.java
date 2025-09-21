package project.domain.scheduleUser.service;

import static project.domain.user.domain.QUser.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.domain.schedule.dao.ScheduleRepository;
import project.domain.schedule.domain.Schedule;
import project.domain.scheduleUser.dao.ScheduleUserRepository;
import project.domain.scheduleUser.domain.ScheduleUser;
import project.domain.user.dao.UserRepository;
import project.domain.user.domain.User;

@Service
@RequiredArgsConstructor
public class ScheduleUserService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ScheduleUserRepository scheduleUserRepository;

    // TODO: 권한 검증 로직 추가
    @Transactional
    public Long addUserToSchedule(Long scheduleId, Long userId) {
        Schedule schedule = scheduleRepository
                .findById(scheduleId)
                .orElseThrow(() -> new PlantException(ErrorCode.SCHEDULE_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new PlantException(ErrorCode.USER_NOT_FOUND));

        ScheduleUser scheduleUser = ScheduleUser.create(schedule, user);
        scheduleUserRepository.save(scheduleUser);

        return scheduleUser.getId();
    }

    @Transactional
    public void removeUserToSchedule(Long scheduleUserId) {
        ScheduleUser scheduleUser = scheduleUserRepository.findById(scheduleUserId)
                .orElseThrow(() -> new PlantException(ErrorCode.SCHEDULE_USER_NOT_FOUND));
        scheduleUserRepository.delete(scheduleUser);
    }
}
