package project.service;

import project.domain.*;
import project.common.auth.oauth.UserInfo;
import project.dto.workspace.*;
import project.repository.*;
import project.exception.user.*;
import project.exception.workspace.NoSuchWorkspaceException;
import project.exception.image.NoSuchImageException;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import static java.util.stream.Collectors.toList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceService{
    
    private final WorkspaceRepository workspaceRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
	private final UserWorkspaceRepository userWorkspaceRepository;
	
	private final EmailService emailService;
    
    @Value("${s3.default-image-url.workspace}")
    private String DEFAULT_WORKSPACE_IMAGE_URL;
    
    // <== 워크스페이스 제작 ==>
    @Transactional
    public Workspace makeWorkspace(CreateWorkspaceRequest request, Long createUserId){
                
        User createUser = userRepository.findById(createUserId)
            .orElseThrow(NoSuchUserException::new);
        
        List<User> userList = userRepository.findByIdIn(request.getUsers());
        
        validateUserList(request.getUsers(), userList);
        
        Image defaultWorkspaceProfile = imageRepository.findByUrl(DEFAULT_WORKSPACE_IMAGE_URL)
                                            .orElseThrow(NoSuchImageException::new);

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
    public void removeWorkspace(Long workspaceId, Long loginUserId){
		
//		Workspace workspace = checkUserAuthority(workspaceId, loginUserId, UserRole.ADMIN);
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(NoSuchWorkspaceException::new);
        
        workspaceRepository.delete(workspace);
    }
    
    // <== 워크스페이스 단일 조회 ==>
    @Transactional(readOnly = true)
    public Workspace findOne(Long workspaceId, Long loginUserId){
		
//		Workspace workspace = checkUserAuthority(workspaceId, loginUserId, UserRole.ADMIN, UserRole.USER);
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(NoSuchWorkspaceException::new);

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
    public Workspace updateWorkspace(Long workspaceId, Long loginUserId, UpdateWorkspaceRequest request){
        
//		Workspace workspace = checkUserAuthority(workspaceId, loginUserId, UserRole.ADMIN);
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(NoSuchWorkspaceException::new);

        Image profile = imageRepository.findByUrl(request.getProfile())
                                        .orElseThrow(NoSuchImageException::new);
        
        workspace.updateWorkspace(request.getName(), profile);
		
        return workspace;
    }
    
    // <== 워크스페이스 유저 추가 ==>
    @Transactional
    public Workspace addUser(Long workspaceId, Long loginUserId, Long userId){
		
//		Workspace workspace = checkUserAuthority(workspaceId, loginUserId, UserRole.ADMIN);
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(NoSuchWorkspaceException::new);

        User user = userRepository.findById(userId)
            .orElseThrow(NoSuchUserException::new);
        
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
    public void removeUser(Long workspaceId, Long loginUserId, Long userId){
//		Workspace workspace = checkUserAuthority(workspaceId, loginUserId, UserRole.ADMIN);
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(NoSuchWorkspaceException::new);

        User user = userRepository.findById(userId)
            .orElseThrow(NoSuchUserException::new);
        
        workspace.removeUser(user);
    }
    
    // <== 워크스페이스 유저 권한 변경 ==>
    @Transactional
    public Workspace changeUserAuthority(Long workspaceId, Long loginUserId, Long userId, UserRole authority){
        
		Workspace workspace = validateChangeUserAutority(workspaceId, loginUserId, userId, authority);
        
        User user = userRepository.findById(userId)
            .orElseThrow(NoSuchUserException::new);      
        
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
    public CalendarResponse getCalendar(Long workspaceId, Long loginUserId, LocalDateTime date){
		
//        Workspace workspace = checkUserAuthority(workspaceId, loginUserId, UserRole.ADMIN, UserRole.USER);
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(NoSuchWorkspaceException::new);

        LocalDateTime startDate = getStartDate(date);
        LocalDateTime endDate = getEndDate(date);
        
        List<Schedule> schedules = scheduleRepository.searchByMonth(workspace, startDate, endDate);
        
        return CalendarResponse.of(workspace, schedules, loginUserId);
    }
    
    // <== 오늘의 일정 반환 ==>
    @Transactional(readOnly = true)
    public CalendarResponse getDailySchedules(Long workspaceId, Long loginUserId, LocalDateTime date){
		
//		Workspace workspace = checkUserAuthority(workspaceId, loginUserId, UserRole.ADMIN, UserRole.USER);
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(NoSuchWorkspaceException::new);

        List<Schedule> schedules = scheduleRepository.searchByDate(workspace, date, date.plusDays(1).minusSeconds(1));
        
        return CalendarResponse.of(workspace, schedules, loginUserId);
    }
    
    
    private void validateUserList(List<Long> userIds, List<User> userList){
        if (userIds.size() != userList.size()){
            throw new NoSuchUserException();
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
			.orElseThrow(NoSuchWorkspaceException::new);

        UserRole loginUserRole = userWorkspace.getUserRole();

        if (UserRole.ADMIN.equals(loginUserRole)){
            return userWorkspace.getWorkspace();
        }
        else if (UserRole.PENDING.equals(loginUserRole)){
            if (loginUserId.equals(userId) && UserRole.USER.equals(authority)) {
                return userWorkspace.getWorkspace();
            }
        }
        throw new InvalidAuthorityException();
	}
}