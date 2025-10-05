package project.domain.schedule.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.common.util.UserUtil;
import project.common.util.WorkspaceUserUtil;
import project.domain.chat.dao.ChatRepository;
import project.domain.chat.domain.Chat;
import project.domain.schedule.dao.ScheduleRepository;
import project.domain.schedule.domain.Progress;
import project.domain.schedule.domain.Schedule;
import project.domain.schedule.dto.ScheduleFullDto;
import project.domain.schedule.dto.request.ScheduleCreateRequest;
import project.domain.schedule.dto.request.ScheduleSearchByWorkspaceRequest;
import project.domain.schedule.dto.request.ScheduleUpdateRequest;
import project.domain.schedule.dto.response.ScheduleSearchByWorkspaceResponse;
import project.domain.scheduleUser.dao.ScheduleUserRepository;
import project.domain.scheduleUser.domain.ScheduleUser;
import project.domain.user.domain.User;
import project.domain.workspace.dao.WorkspaceRepository;
import project.domain.workspace.domain.Workspace;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleUserRepository scheduleUserRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ChatRepository chatRepository;
    private final UserUtil userUtil;
    private final WorkspaceUserUtil workspaceUserUtil;

    // <== 스케줄 상세 조회 ==>
    @Transactional(readOnly = true)
    public ScheduleFullDto findOne(Long scheduleId) {
        Schedule schedule = findScheduleById(scheduleId);
        Long workspaceId = schedule.getWorkspace().getId();
        validateLoginUserInWorkspace(workspaceId);

        List<User> usersInSchedule = scheduleUserRepository.findFetchByScheduleId(scheduleId).stream()
                .map(ScheduleUser::getUser)
                .toList();
        List<Chat> chats = chatRepository.findByScheduleId(scheduleId);

        return ScheduleFullDto.from(schedule, usersInSchedule, chats);
    }

    // <== 스케줄 생성 ==>
    @Transactional
    public Long createSchedule(ScheduleCreateRequest request) {
        Workspace workspace = workspaceRepository
                .findById(request.workspaceId())
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));
        validateLoginUserInWorkspace(workspace.getId());

        Schedule schedule =
                Schedule.create(workspace, request.name(), request.startDate(), request.endDate(), request.content());
        scheduleRepository.save(schedule);

        return schedule.getId();
    }

    // <== 스케줄 수정 ==>
    @Transactional
    public void updateSchedule(Long scheduleId, ScheduleUpdateRequest request) {
        Schedule schedule = findScheduleById(scheduleId);
        Long workspaceId = schedule.getWorkspace().getId();
        validateLoginUserInWorkspace(workspaceId);

        schedule.update(request.name(), request.startDate(), request.endDate(), request.content(), request.state());
        scheduleRepository.save(schedule);
    }

    // <== 스케줄 삭제 ==>
    @Transactional
    public void removeSchedule(Long id) {
        Schedule schedule = findScheduleById(id);
        Long workspaceId = schedule.getWorkspace().getId();
        validateLoginUserInWorkspace(workspaceId);

        scheduleRepository.delete(schedule);
    }

    // <== 스케줄 상태 수정 ==>
    @Transactional
    public void moveScheduleState(Long id, Progress state) {
        Schedule schedule = findScheduleById(id);
        Long workspaceId = schedule.getWorkspace().getId();
        validateLoginUserInWorkspace(workspaceId);

        schedule.moveProgress(state);
        scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public ScheduleSearchByWorkspaceResponse searchScheduleByWorkspace(ScheduleSearchByWorkspaceRequest request) {
        Long workspaceId = request.workspaceId();
        Workspace workspace = workspaceRepository
                .findById(workspaceId)
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));
        validateLoginUserInWorkspace(workspaceId);

        List<Schedule> schedules = scheduleRepository.searchByWorkspaceInAndDateBetween(
                List.of(workspaceId), request.startDate(), request.endDate());

        return ScheduleSearchByWorkspaceResponse.of(workspace, schedules);
    }

    private Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new PlantException(ErrorCode.SCHEDULE_NOT_FOUND));
    }

    private void validateLoginUserInWorkspace(Long workspaceId) {
        Long loginUserId = userUtil.getLoginUserId();
        boolean exists = workspaceUserUtil.existsUser(workspaceId, loginUserId);

        if (!exists) {
            throw new PlantException(ErrorCode.WORKSPACE_USER_AUTHORITY_INVALID);
        }
    }
}
