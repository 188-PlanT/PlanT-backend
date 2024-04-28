package project.domain.workspace.service;

import project.common.util.UserUtil;
import project.domain.image.dao.ImageRepository;
import project.domain.image.domain.Image;
import project.domain.schedule.dao.ScheduleRepository;
import project.domain.schedule.domain.Schedule;
import project.domain.user.dao.UserRepository;
import project.domain.user.domain.User;
import project.domain.user.domain.UserRole;
import project.domain.workspace.dao.UserWorkspaceRepository;
import project.domain.workspace.dao.WorkspaceRepository;
import project.domain.workspace.domain.UserWorkspace;
import project.domain.workspace.domain.Workspace;
import project.domain.workspace.dto.CalendarResponse;
import project.domain.workspace.dto.CreateWorkspaceRequest;
import project.domain.workspace.dto.UpdateWorkspaceRequest;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import project.common.service.EmailService;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceService{
    
    private final WorkspaceRepository workspaceRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
	private final UserWorkspaceRepository userWorkspaceRepository;
    private final UserUtil userUtil;
	
	private final EmailService emailService;
    
    @Value("${s3.default-image-url.workspace}")
    private String DEFAULT_WORKSPACE_IMAGE_URL;
    
    // <== 워크스페이스 제작 ==>
    @Transactional
    public Workspace makeWorkspace(CreateWorkspaceRequest request){
                
        User createUser = userUtil.getLoginUser();
        
        List<User> userList = userRepository.findByIdIn(request.getUsers());
        
        validateUserList(request.getUsers(), userList);
        
        Image defaultWorkspaceProfile = imageRepository.findByUrl(DEFAULT_WORKSPACE_IMAGE_URL)
                                            .orElseThrow(() -> new PlantException(ErrorCode.IMAGE_NOT_FOUND));

        Workspace workspace = Workspace.builder()
                                        .name(request.getName())
                                        .profile(defaultWorkspaceProfile)
                                        .user(createUser)
                                        .build();
        
        workspace.addUserByList(userList);        
        workspaceRepository.save(workspace);
        
        return workspace;
    }
    
    // <== 워크스페이스 삭제 ==>
    @Transactional
    public void removeWorkspace(Long workspaceId){

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));
        
        workspaceRepository.delete(workspace);
    }
    
    // <== 워크스페이스 단일 조회 ==>
    @Transactional(readOnly = true)
    public Workspace findOne(Long workspaceId){

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));

        // Lazy Loding
        workspace.getUserWorkspaces()
            .forEach(uw -> {
                User user = uw.getUser();
                Image image = user.getProfile();
                String url = image.getUrl();
            });
        return workspace;
    }
    
    //<== 워크스페이스 수정 ==>
    @Transactional
    public Workspace updateWorkspace(Long workspaceId, UpdateWorkspaceRequest request){

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));

        Image profile = imageRepository.findByUrl(request.getProfile())
                                        .orElseThrow(() -> new PlantException(ErrorCode.IMAGE_NOT_FOUND));
        
        workspace.updateWorkspace(request.getName(), profile);
		
        return workspace;
    }
    
    // <== 워크스페이스 유저 추가 ==>
    @Transactional
    public Workspace addUser(Long workspaceId, Long userId){

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new PlantException(ErrorCode.USER_NOT_FOUND));
        
        workspace.addUser(user);
        
		emailService.sendInvitationMail(user.getEmail(), workspace.getName());
        
        // Lazy Loding
        workspace.getUserWorkspaces().stream()
            .forEach(uw -> {
                User tempUser = uw.getUser();
                Image image = tempUser.getProfile();
                image.getUrl();
            });
        
        return workspace;
    }
    
    // <== 워크스페이스 유저 삭제 ==>
    @Transactional
    public void removeUser(Long workspaceId, Long userId){
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PlantException(ErrorCode.USER_NOT_FOUND));
        
        workspace.removeUser(user);
    }
    
    // <== 워크스페이스 유저 권한 변경 ==>
    @Transactional
    public Workspace changeUserAuthority(Long workspaceId, Long userId, UserRole authority){
        Long loginUserId = userUtil.getLoginUserId();
        
		Workspace workspace = validateChangeUserAutority(workspaceId, loginUserId, userId, authority);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PlantException(ErrorCode.USER_NOT_FOUND));
        
        workspace.giveAuthority(user, authority);
        
        // Lazy Loding
        workspace.getUserWorkspaces().stream()
            .forEach(uw -> {
                User tempUser = uw.getUser();
                Image image = tempUser.getProfile();
                image.getUrl();
            });
        
        return workspace;
    }
    
    // <== 캘린더 응답 반환 ==>
    @Transactional(readOnly = true)
    public CalendarResponse getCalendar(Long workspaceId, LocalDateTime date){
        Long loginUserId = userUtil.getLoginUserId();

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));

        LocalDateTime startDate = getStartDate(date);
        LocalDateTime endDate = getEndDate(date);
        
        List<Schedule> schedules = scheduleRepository.searchByMonth(workspace, startDate, endDate);
        
        return CalendarResponse.of(workspace, schedules, loginUserId);
    }
    
    // <== 오늘의 일정 반환 ==>
    @Transactional(readOnly = true)
    public CalendarResponse getDailySchedules(Long workspaceId, LocalDateTime date){
        Long loginUserId = userUtil.getLoginUserId();

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));

        List<Schedule> schedules = scheduleRepository.searchByDate(workspace, date, date.plusDays(1).minusSeconds(1));
        
        return CalendarResponse.of(workspace, schedules, loginUserId);
    }
    
    
    private void validateUserList(List<Long> userIds, List<User> userList){
        if (userIds.size() != userList.size()){
            throw new PlantException(ErrorCode.USER_NOT_FOUND, "잘못된 유저 정보가 포함되어 있습니다");
        }
    }
    
    private LocalDateTime getStartDate(LocalDateTime dateTime){
        LocalDate date = dateTime.toLocalDate();
        date = date.withDayOfMonth(1);
        return date.atStartOfDay();
    }
    
    private LocalDateTime getEndDate(LocalDateTime dateTime){
        LocalDate date = dateTime.toLocalDate();
        date = date.withDayOfMonth(date.lengthOfMonth());
        return date.atTime(LocalTime.MAX);
    }

    //로직 분리할까 그냥...?
	private Workspace validateChangeUserAutority(Long workspaceId, Long loginUserId, Long userId, UserRole authority){
        UserWorkspace userWorkspace = userWorkspaceRepository.searchByUserIdAndWorkspaceId(loginUserId, workspaceId)
			.orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));

        UserRole loginUserRole = userWorkspace.getUserRole();

        if (UserRole.ADMIN.equals(loginUserRole)){
            return userWorkspace.getWorkspace();
        }
        else if (UserRole.PENDING.equals(loginUserRole)){
            if (loginUserId.equals(userId) && UserRole.USER.equals(authority)) {
                return userWorkspace.getWorkspace();
            }
        }
        throw new PlantException(ErrorCode.USER_AUTHORITY_INVALID);
	}
}