package project.service;

import project.domain.*;
import project.common.auth.oauth.UserInfo;
import project.dto.workspace.*;
import project.repository.*;
import project.exception.user.NoSuchUserException;
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
	
	private final EmailService emailService;
    
    @Value("${s3.default-image-url.workspace}")
    private String DEFAULT_WORKSPACE_IMAGE_URL;
    
    // <== 워크스페이스 제작 ==>
    @Transactional
    public Workspace makeWorkspace(CreateWorkspaceRequest request, String createUserEmail){
                
        User createUser = userRepository.findByEmail(createUserEmail)
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
    public void removeWorkspace(Long workspaceId, String userEmail){
        User loginUser = userRepository.findByEmail(userEmail)
            .orElseThrow(NoSuchUserException::new);
        
        Workspace findWorkspace = workspaceRepository.findById(workspaceId)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        findWorkspace.checkAdmin(loginUser);
        
        workspaceRepository.delete(findWorkspace);
    }
    
    // <== 워크스페이스 단일 조회 ==>
    @Transactional(readOnly = true)
    public Workspace findOne(Long workspaceId, String userEmail){
        User loginUser = userRepository.findByEmail(userEmail)
            .orElseThrow(NoSuchUserException::new);
        
        Workspace findWorkspace = workspaceRepository.findById(workspaceId)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        findWorkspace.checkUser(loginUser);
        
        // Lazy Loding
        findWorkspace.getUserWorkspaces().stream()
            .forEach(uw -> {
                User user = uw.getUser();
                Image image = user.getProfile();
                image.getUrl();
            });
        
        findWorkspace.getProfile().getUrl();
        
        return findWorkspace;
    }
    
    //<== 워크스페이스 수정 ==>
    @Transactional
    public Workspace updateWorkspace(Long id, String userEmail, UpdateWorkspaceRequest request){
        
        User loginUser = userRepository.findByEmail(userEmail)
            .orElseThrow(NoSuchUserException::new);
        
        Workspace workspace = workspaceRepository.findById(id)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        workspace.checkAdmin(loginUser);
        
        Image profile = imageRepository.findByUrl(request.getProfile())
                                        .orElseThrow(NoSuchImageException::new);
        
        workspace.updateWorkspace(request.getName(), profile);
        return workspace;
    }
    
    // <== 워크스페이스 유저 추가 ==>
    @Transactional
    public Workspace addUser(Long workspaceId, String userEmail, Long userId){
        User loginUser = userRepository.findByEmail(userEmail)
            .orElseThrow(NoSuchUserException::new);
        
        Workspace workspace = workspaceRepository.findById(workspaceId)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        workspace.checkUser(loginUser);
        
        User user = userRepository.findById(userId)
            .orElseThrow(NoSuchUserException::new);
        
        workspace.addUser(user);
        
		emailService.sendInvitationMail(user.getEmail(), loginUser.getEmail(), workspace.getName());
        
        // Lazy Loding
        workspace.getUserWorkspaces().stream()
            .forEach(uw -> {
                User tempUser = uw.getUser();
                Image image = tempUser.getProfile();
                image.getUrl();
            });
        
        workspace.getProfile().getUrl();
        
        return workspace;
    }
    
    // <== 워크스페이스 유저 삭제 ==>
    @Transactional
    public void removeUser(Long workspaceId, String userEmail, Long userId){
        User loginUser = userRepository.findByEmail(userEmail)
            .orElseThrow(NoSuchUserException::new);
        
        Workspace workspace = workspaceRepository.findById(workspaceId)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        workspace.checkAdmin(loginUser);
        
        User user = userRepository.findById(userId)
            .orElseThrow(NoSuchUserException::new);
        
        workspace.removeUser(user);
    }
    
    // <== 워크스페이스 유저 권한 변경 ==>
    @Transactional
    public Workspace changeUserAuthority(Long workspaceId, String loginUserEmail, Long userId, UserRole authority){
        User loginUser = userRepository.findByEmail(loginUserEmail)
            .orElseThrow(NoSuchUserException::new);
        
        Workspace workspace = workspaceRepository.findById(workspaceId)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        User user = userRepository.findById(userId)
            .orElseThrow(NoSuchUserException::new);
        
        //스스로 관리자 권한 부여 가능 -> 로직 수정 필요
        if(loginUser != user){
            workspace.checkAdmin(loginUser);
        }        
        
        workspace.giveAuthority(user, authority);
        
        // Lazy Loding
        workspace.getUserWorkspaces().stream()
            .forEach(uw -> {
                User tempUser = uw.getUser();
                Image image = tempUser.getProfile();
                image.getUrl();
            });
        
        workspace.getProfile().getUrl();
        
        return workspace;
    }
    
    // <== 캘린더 응답 반환 ==>
    @Transactional(readOnly = true)
    public CalendarResponse getCalendar(Long workspaceId, String loginUserEmail, LocalDateTime date){
        User loginUser = userRepository.findByEmail(loginUserEmail)
            .orElseThrow(NoSuchUserException::new);
        
        Workspace workspace = workspaceRepository.findById(workspaceId)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        workspace.checkUser(loginUser);
        
        LocalDateTime startDate = getStartDate(date);
        LocalDateTime endDate = getEndDate(date);
        
        List<Schedule> schedules = scheduleRepository.searchSchedule(workspace, startDate, endDate);
        
        return CalendarResponse.of(workspace, schedules);
    }
    
    // <== 오늘의 일정 반환 ==>
    @Transactional(readOnly = true)
    public CalendarResponse getDailySchedules(Long workspaceId, String loginUserEmail, LocalDateTime date){
        User loginUser = userRepository.findByEmail(loginUserEmail)
            .orElseThrow(NoSuchUserException::new);
        
        Workspace workspace = workspaceRepository.findById(workspaceId)
            .orElseThrow(NoSuchWorkspaceException::new);
        
        workspace.checkUser(loginUser);
        
        List<Schedule> schedules = scheduleRepository.searchSchedule(workspace, date, date.plusDays(1).minusSeconds(1));
        
        return CalendarResponse.of(workspace, schedules);
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
}