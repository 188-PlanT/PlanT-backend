package project.domain.workspace.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.common.util.UserUtil;
import project.common.util.WorkspaceUserUtil;
import project.domain.image.dao.ImageRepository;
import project.domain.image.domain.Image;
import project.domain.schedule.dao.ScheduleRepository;
import project.domain.schedule.domain.Schedule;
import project.domain.user.domain.User;
import project.domain.workspace.dao.WorkspaceRepository;
import project.domain.workspace.domain.Workspace;
import project.domain.workspace.dto.request.WorkspaceCreateRequest;
import project.domain.workspace.dto.request.WorkspaceUpdateRequest;
import project.domain.workspace.dto.response.CalendarResponse;
import project.domain.workspaceUser.dao.WorkspaceUserRepository;
import project.domain.workspaceUser.domain.WorkspaceUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final ScheduleRepository scheduleRepository;
    private final ImageRepository imageRepository;
    private final WorkspaceUserRepository workspaceUserRepository;
    private final UserUtil userUtil;
    private final WorkspaceUserUtil workspaceUserUtil;

    // <== 워크스페이스 제작 ==>
    @Transactional
    public Long makeWorkspace(WorkspaceCreateRequest request) {
        Workspace workspace = Workspace.create(request.name());
        workspaceRepository.save(workspace);

        // TODO: 도메인 이벤트로 분리 고려
        User creator = userUtil.getLoginUser();
        WorkspaceUser workspaceUser = WorkspaceUser.createAdmin(workspace, creator);
        workspaceUserRepository.save(workspaceUser);

        return workspace.getId();
    }

    // <== 워크스페이스 삭제 ==>
    @Transactional
    public void removeWorkspace(Long workspaceId) {
        validateLoginUserIsAdmin(workspaceId);

        Workspace workspace = findWorkspaceById(workspaceId);
        workspaceRepository.delete(workspace);
    }

    // <== 워크스페이스 수정 ==>
    @Transactional
    public void updateWorkspace(Long workspaceId, WorkspaceUpdateRequest request) {
        validateLoginUserIsAdmin(workspaceId);

        Workspace workspace = findWorkspaceById(workspaceId);
        Image profile = request.profileUrl() != null ? getProfileByUrl(request.profileUrl()) : null;
        workspace.update(request.name(), profile);
    }

    private Image getProfileByUrl(String profileUrl) {
        return imageRepository.findByUrl(profileUrl).orElseThrow(() -> new PlantException(ErrorCode.IMAGE_NOT_FOUND));
    }

    // <== 캘린더 응답 반환 ==>
    @Transactional(readOnly = true)
    public CalendarResponse getCalendar(Long workspaceId, LocalDateTime date) {
        validateLoginUserInWorkspace(workspaceId);
        Workspace workspace = findWorkspaceById(workspaceId);

        LocalDateTime startDate = getStartDate(date);
        LocalDateTime endDate = getEndDate(date);

        List<Schedule> schedules = scheduleRepository.searchByMonth(workspace, startDate, endDate);
        return CalendarResponse.of(workspace, schedules);
    }

    // <== 오늘의 일정 반환 ==>
    @Transactional(readOnly = true)
    public CalendarResponse getDailySchedules(Long workspaceId, LocalDateTime date) {
        validateLoginUserInWorkspace(workspaceId);
        Workspace workspace = findWorkspaceById(workspaceId);

        List<Schedule> schedules = scheduleRepository.searchByDate(
                workspace, date, date.plusDays(1).minusSeconds(1));
        return CalendarResponse.of(workspace, schedules);
    }

    private LocalDateTime getStartDate(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        date = date.withDayOfMonth(1);
        return date.atStartOfDay();
    }

    private LocalDateTime getEndDate(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        date = date.withDayOfMonth(date.lengthOfMonth());
        return date.atTime(LocalTime.MAX);
    }

    private Workspace findWorkspaceById(Long id) {
        return workspaceRepository.findById(id).orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));
    }

    private void validateLoginUserIsAdmin(Long workspaceId) {
        Long loginUserId = userUtil.getLoginUserId();
        boolean isAdmin = workspaceUserUtil.isAdminUser(workspaceId, loginUserId);

        if (!isAdmin) {
            throw new PlantException(ErrorCode.WORKSPACE_USER_AUTHORITY_INVALID);
        }
    }

    private void validateLoginUserInWorkspace(Long workspaceId) {
        Long loginUserId = userUtil.getLoginUserId();
        boolean exists = workspaceUserUtil.existsUser(workspaceId, loginUserId);

        if (!exists) {
            throw new PlantException(ErrorCode.WORKSPACE_USER_AUTHORITY_INVALID);
        }
    }
}
