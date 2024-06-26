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
// import project.common.service.EmailService;

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
	
	// private final EmailService emailService;
    
    @Value("${s3.default-image-url.workspace}")
    private String DEFAULT_WORKSPACE_IMAGE_URL;
    
    // <== 워크스페이스 제작 ==>
    @Transactional
    public Workspace makeWorkspace(CreateWorkspaceRequest request){
                
        User createUser = userUtil.getLoginUser();
        
        List<User> userList = userUtil.getUserByList(request.getUsers());
        
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

        Workspace workspace = findWorkspaceById(workspaceId);
        
        workspaceRepository.delete(workspace);
    }
    
	// 여기 쓰이는 로직 찾아서 findOneDetail로 수정 필요
    // <== 워크스페이스 단일 조회 ==>
    @Transactional(readOnly = true)
    public Workspace findOne(Long workspaceId){

        Workspace workspace = findWorkspaceById(workspaceId);

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

        Workspace workspace = findWorkspaceById(workspaceId);
		
		String name = workspace.getName();
		// Optional 하도록 수정
		Image profile = workspace.getProfile();
		
		if (request.getProfile() != null){
			profile = imageRepository.findByUrl(request.getProfile())
            	.orElseThrow(() -> new PlantException(ErrorCode.IMAGE_NOT_FOUND));	
		}
		
		if (request.getName() != null){
			name = request.getName();
		}
			
		workspace.updateWorkspace(name, profile);
		
        return workspace;
    }
    
    // <== 워크스페이스 유저 추가 ==>
    @Transactional
    public Workspace addUser(Long workspaceId, Long userId){

        Workspace workspace = findWorkspaceById(workspaceId);

        User user = userUtil.getUserById(userId);
        
        workspace.addUser(user);
        
		// emailService.sendInvitationMail(user.getEmail(), workspace.getName());
        
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
        Workspace workspace = findWorkspaceById(workspaceId);

        User user = userUtil.getUserById(userId);
        
        workspace.removeUser(user);
    }
    
    // <== 워크스페이스 유저 권한 변경 ==>
    @Transactional
    public Workspace changeUserAuthority(Long workspaceId, Long userId, UserRole authority){
        Long loginUserId = userUtil.getLoginUserId();
        
		// Workspace workspace = validateChangeUserAutority(workspaceId, loginUserId, userId, authority);
		Workspace workspace = findWorkspaceById(workspaceId);
		
        User user = userUtil.getUserById(userId);
        
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

        Workspace workspace = findWorkspaceById(workspaceId);

        LocalDateTime startDate = getStartDate(date);
        LocalDateTime endDate = getEndDate(date);
        
        List<Schedule> schedules = scheduleRepository.searchByMonth(workspace, startDate, endDate);
        
        return CalendarResponse.of(workspace, schedules, loginUserId);
    }
    
    // <== 오늘의 일정 반환 ==>
    @Transactional(readOnly = true)
    public CalendarResponse getDailySchedules(Long workspaceId, LocalDateTime date){
        Long loginUserId = userUtil.getLoginUserId();

        Workspace workspace = findWorkspaceById(workspaceId);

        List<Schedule> schedules = scheduleRepository.searchByDate(workspace, date, date.plusDays(1).minusSeconds(1));
        
        return CalendarResponse.of(workspace, schedules, loginUserId);
    }
	
	// <== 어드민 페이지용 조회 ==>
	@Transactional(readOnly = true)
	public Workspace findOneDetail(Long workspaceId){
		Workspace workspace = findWorkspaceById(workspaceId);
		
		workspace.getProfile().getUrl();
		
		for (UserWorkspace uw : workspace.getUserWorkspaces()){
			uw.getUserRole();
			uw.getUser().getEmail();
		}
		
		for (Schedule s : workspace.getSchedules()){
			s.getName();
		}
		
		return workspace;
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

    private Workspace findWorkspaceById(Long id){
        return workspaceRepository.findById(id)
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));
    }

    //로직 분리할까 그냥...?
	// private Workspace validateChangeUserAutority(Long workspaceId, Long loginUserId, Long userId, UserRole authority){
	// UserWorkspace userWorkspace = userWorkspaceRepository.searchByUserIdAndWorkspaceId(loginUserId, workspaceId)
	// 		.orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_NOT_FOUND));

	// UserRole loginUserRole = userWorkspace.getUserRole();

	// if (UserRole.ADMIN.equals(loginUserRole)){
	// return userWorkspace.getWorkspace();
	// }
	// else if (UserRole.PENDING.equals(loginUserRole)){
	// if (loginUserId.equals(userId) && UserRole.USER.equals(authority)) {
	// return userWorkspace.getWorkspace();
	// }
	// }
	// throw new PlantException(ErrorCode.USER_AUTHORITY_INVALID);
	// }
}