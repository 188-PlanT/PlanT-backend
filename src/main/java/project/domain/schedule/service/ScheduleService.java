package project.domain.schedule.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.common.util.UserUtil;
import project.domain.chat.dao.ChatRepository;
import project.domain.chat.domain.Chat;
import project.domain.schedule.dao.ScheduleRepository;
import project.domain.schedule.domain.Progress;
import project.domain.schedule.domain.Schedule;
import project.domain.schedule.dto.ScheduleDto;
import project.domain.schedule.dto.request.CreateScheduleRequest;
import project.domain.schedule.dto.request.UpdateScheduleRequest;
import project.domain.user.domain.User;
import project.domain.user.domain.UserRole;
import project.domain.workspace.dao.WorkspaceRepository;
import project.domain.workspace.domain.Workspace;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ChatRepository chatRepository;
    private final UserUtil userUtil;

    // <== 스케줄 단일 조회 ==>
    @Transactional(readOnly = true)
    public ScheduleDto findOne(Long id) {
        Schedule schedule = findScheduleById(id);
        List<Chat> chats = chatRepository.findByScheduleId(id);

        return ScheduleDto.from(schedule, chats);
    }

    // <== 스케줄 생성 ==>
    @Transactional
    public Long createSchedule(CreateScheduleRequest request) {
        Workspace workspace = workspaceRepository
                .findById(request.workspaceId())
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));

        // api url에 workspaceId 가 들어가지 않으므로 Interceptor에서 검증 불가능 -> 서비스에서 검증
        validateLoginUserRole(workspace.getId());

        List<User> users = userUtil.getUserByList(request.users());

        Schedule schedule = Schedule.builder()
                .workspace(workspace)
                .name(request.name())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .content(request.content())
                .users(users)
                .state(request.state())
                .build();

        scheduleRepository.save(schedule);

        return schedule.getId();
    }

    private void validateLoginUserRole(Long workspaceId) {
        UserRole userRole = userUtil.getLoginUserRole(workspaceId);

        if (userRole == null) {
            throw new PlantException(ErrorCode.USER_AUTHORITY_INVALID);
        }
    }

    // <== 스케줄 수정 ==>
    @Transactional
    public ScheduleDto updateSchedule(Long scheduleId, UpdateScheduleRequest request) {
        Schedule schedule = findScheduleById(scheduleId);
        List<User> users = userUtil.getUserByList(request.users());
        schedule.update(
                request.name(), request.startDate(), request.endDate(), request.content(), users, request.state());

        List<Chat> chats = chatRepository.findByScheduleId(scheduleId);
        return ScheduleDto.from(schedule, chats);
    }

    // <== 스케줄 삭제 ==>
    @Transactional
    public void removeSchedule(Long id) {
        Schedule schedule = findScheduleById(id);
        scheduleRepository.delete(schedule);
    }

    // <== 스케줄 상태 수정 ==>
    @Transactional
    public ScheduleDto moveScheduleState(Long id, Progress state) {
        Schedule schedule = findScheduleById(id);
        schedule.moveProgress(state);

        List<Chat> chats = chatRepository.findByScheduleId(id);
        return ScheduleDto.from(schedule, chats);
    }

    private Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new PlantException(ErrorCode.SCHEDULE_NOT_FOUND));
    }
}
