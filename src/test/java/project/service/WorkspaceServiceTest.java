// package project.service;

// import project.repository.UserRepository;
// import project.repository.WorkspaceRepository;
// import project.repository.ScheduleRepository;
// import project.domain.*;
// import project.dto.workspace.*;
// import project.exception.user.*;
// import project.exception.workspace.*;

// import java.util.Optional;
// import java.util.List;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.time.LocalDateTime;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import static org.mockito.BDDMockito.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.junit.jupiter.api.Assertions.*;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.PageRequest;

// @ExtendWith(MockitoExtension.class)
// public class WorkspaceServiceTest{
    
//     @Mock
//     private UserRepository userRepository;
    
//     @Mock
//     private WorkspaceRepository workspaceRepository;
    
//     @Mock
//     private ScheduleRepository scheduleRepository;
    
//     @InjectMocks
//     private WorkspaceService workspaceService;
    
//     @Test
//     public void 워크스페이스_제작(){
//         //given
//         User admin = new User("admin@gmail.com", "admin", "test1111", "profile", UserRole.ADMIN);
//         User user1 = new User("test11@gmail.com", "test11", "test1111", "profile", UserRole.USER);
//         User user2 = new User("test22@gmail.com", "test22", "test2222", "profile", UserRole.USER);
        
//         List<User> userList = new ArrayList<>();
//         userList.add(user1);
//         userList.add(user2);
        
//         List<Long> userIdList = new ArrayList<>(Arrays.asList(1L, 2L));
        
//         CreateWorkspaceRequest successRequest = new CreateWorkspaceRequest("testWorkspace1", "profile1", userIdList);
        
//         given(userRepository.findByEmail("admin@gmail.com")).willReturn(Optional.of(admin));
//         given(userRepository.findByEmail("failEmail@gmail.com")).willReturn(Optional.ofNullable(null));
//         given(userRepository.findByIdIn(any(List.class))).willReturn(userList);
        
//         //when
//         Workspace result = workspaceService.makeWorkspace(successRequest, "admin@gmail.com");
        
//         //then
//         assertEquals(result.getName(), "testWorkspace1");
        
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.makeWorkspace(successRequest, "failEmail@gmail.com");
//         });
//     }
    
//     @Test
//     public void 워크스페이스_삭제(){
//         //given
//         User admin = new User("admin@gmail.com", "admin", "test1111", "profile", UserRole.ADMIN);
//         User user = new User("test11@gmail.com", "test", "test1111", "profile", UserRole.USER);

//         Workspace workspace = Workspace.builder()
//                                         .name("testWorksapce1")
//                                         .user(admin)
//                                         .build();
        
//         given(userRepository.findByEmail("admin@gmail.com")).willReturn(Optional.of(admin));
//         given(userRepository.findByEmail("test11@gmail.com")).willReturn(Optional.of(user));
//         given(userRepository.findByEmail("failEmail@gmail.com")).willReturn(Optional.ofNullable(null));
//         given(workspaceRepository.findById(1L)).willReturn(Optional.of(workspace));
//         given(workspaceRepository.findById(2L)).willReturn(Optional.ofNullable(null));
        
//         //when
//         workspaceService.removeWorkspace(1L, "admin@gmail.com");
        
//         //then
//         // 올바르지 않은 이메일 입력
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.removeWorkspace(1L, "failEmail@gmail.com");
//         });
        
//         // 올바르지 않은 workspace id 입력
//         assertThrows(NoSuchWorkspaceException.class, () -> {
//             workspaceService.removeWorkspace(2L, "admin@gmail.com");
//         });
        
//         // 권한 없는 유저 이메일 입력 (workspace에 속하지 않은 유저)
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.removeWorkspace(1L, "test11@gmail.com");
//         });
//     }
    
//     @Test
//     public void 워크스페이스_단일_조회(){
//         //given
//         User admin = new User("admin@gmail.com", "admin", "test1111", "profile", UserRole.ADMIN);
//         User user1 = new User("test11@gmail.com", "test11", "test1111", "profile", UserRole.USER);
//         User user2 = new User("test22@gmail.com", "test22", "test2222", "profile", UserRole.USER);
        
//         Workspace workspace = Workspace.builder()
//                                         .name("testWorkspace1")
//                                         .user(admin)
//                                         .build();
        
//         workspace.addUser(user1);
        
//         given(workspaceRepository.findById(1L)).willReturn(Optional.of(workspace));
//         given(workspaceRepository.findById(2L)).willReturn(Optional.ofNullable(null));
//         given(userRepository.findByEmail("admin@gmail.com")).willReturn(Optional.ofNullable(admin));
//         given(userRepository.findByEmail("test22@gmail.com")).willReturn(Optional.ofNullable(user2));
//         given(userRepository.findByEmail("failEmail@gmail.com")).willReturn(Optional.ofNullable(null));
        
//         //when
//         Workspace result = workspaceService.findOne(1L, "admin@gmail.com");
        
//         //then
//         assertEquals(result.getName(), "testWorkspace1");
//         assertEquals(result.getUserWorkspaces().size(), 2);
        
//         // 올바르지 않은 workspace id 입력
//         assertThrows(NoSuchWorkspaceException.class, () -> {
//             workspaceService.findOne(2L, "admin@gmail.com");
//         });
        
//         // 올바르지 않은 유저 이메일 입력
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.findOne(1L, "failEmail@gmail.com");
//         });
        
//         // 권한 없는 유저 이메일 입력 (workspace에 속하지 않은 유저)
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.findOne(1L, "test22@gmail.com");
//         });
//     }

//     public void 워크스페이스_수정(){
//         //given
//         User admin = new User("admin@gmail.com", "admin", "test1111", "profile", UserRole.ADMIN);
//         User user1 = new User("test11@gmail.com", "test11", "test1111", "profile", UserRole.USER);
//         User user2 = new User("test22@gmail.com", "test22", "test2222", "profile", UserRole.USER);
        
//         Workspace workspace = Workspace.builder()
//                                         .name("testWorkspace1")
//                                         .user(admin)
//                                         .build();
        
//         workspace.addUser(user1);
        
//         given(workspaceRepository.findById(1L)).willReturn(Optional.of(workspace));
//         given(workspaceRepository.findById(2L)).willReturn(Optional.ofNullable(null));
//         given(userRepository.findByEmail("admin@gmail.com")).willReturn(Optional.of(admin));
//         given(userRepository.findByEmail("test22@gmail.com")).willReturn(Optional.ofNullable(user2));
//         given(userRepository.findByEmail("failEmail@gmail.com")).willReturn(Optional.ofNullable(null));
        
//         UpdateWorkspaceRequest request = new UpdateWorkspaceRequest("testWorkspace2", "profile");
        
//         //when
//         Workspace result = workspaceService.updateWorkspace(1L, "admin@gmail.com", request);
        
//         //then
//         assertEquals(result.getName(), "testWorkspace2");
        
//         // 올바르지 않은 workspace id 입력
//         assertThrows(NoSuchWorkspaceException.class, () -> {
//             workspaceService.findOne(2L, "admin@gmail.com");
//         });
        
//         // 올바르지 않은 유저 이메일 입력
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.findOne(1L, "failEmail@gmail.com");
//         });
        
//         // 권한 없는 유저 이메일 입력 (workspace에 속하지 않은 유저)
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.findOne(1L, "test22@gmail.com");
//         });
//     }
    
//     public void 워크스페이스_유저_추가(){
//         //given
//         User admin = new User("admin@gmail.com", "admin", "test1111", "profile", UserRole.ADMIN);
//         User user1 = new User("test11@gmail.com", "test11", "test1111", "profile", UserRole.USER);
//         User user2 = new User("test22@gmail.com", "test22", "test2222", "profile", UserRole.USER);
//         User user3 = new User("test33@gmail.com", "test33", "test3333", "profile", UserRole.USER);
        
//         Workspace workspace = Workspace.builder()
//                                         .name("testWorkspace1")
//                                         .user(admin)
//                                         .build();
        
//         workspace.addUser(user1);
        
//         given(workspaceRepository.findById(1L)).willReturn(Optional.of(workspace));
//         given(workspaceRepository.findById(2L)).willReturn(Optional.ofNullable(null));
//         given(userRepository.findByEmail("admin@gmail.com")).willReturn(Optional.of(admin));
//         given(userRepository.findByEmail("test33@gmail.com")).willReturn(Optional.of(user3));
//         given(userRepository.findByEmail("failEmail@gmail.com")).willReturn(Optional.ofNullable(null));
//         given(userRepository.findById(1L)).willReturn(Optional.of(user1));
//         given(userRepository.findById(2L)).willReturn(Optional.of(user2));
//         given(userRepository.findById(3L)).willReturn(Optional.of(user3));
//         given(userRepository.findById(5L)).willReturn(Optional.ofNullable(null));
        
//         //when
//         Workspace result = workspaceService.addUser(1L, "admin@gmail.com", 2L);
        
//         //then
//         assertEquals(result.getName(), "testWorkspace2");
//         assertEquals(result.getUserWorkspaces().size(), 3);
//         assertEquals(result.getUserWorkspaces().get(0).getUser().getEmail(), "admin@gmail.com");
//         assertEquals(result.getUserWorkspaces().get(1).getUser().getEmail(), "test11@gmail.com");
//         assertEquals(result.getUserWorkspaces().get(2).getUser().getEmail(), "test22@gmail.com");
        
//         // 올바르지 않은 workspace id 입력
//         assertThrows(NoSuchWorkspaceException.class, () -> {
//             workspaceService.addUser(2L, "admin@gmail.com", 2L);
//         });
        
//         // 올바르지 않은 유저 이메일 입력
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.addUser(1L, "failEmail@gmail.com", 2L);
//         });
        
//         // 올바르지 않은 추가할 유저 아이디
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.addUser(1L, "admin@gmail.com", 5L);
//         });
        
//         // 이미 존재하는 유저 반복 추가
//         assertThrows(UserAlreadyExistException.class, () -> {
//             workspaceService.addUser(1L, "admin@gmail.com", 1L);
//         });
        
//         // 권한 없는 유저 이메일 입력 (workspace에 속하지 않은 유저)
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.addUser(1L, "test33@gmail.com", 3L);
//         });
//     }
    
//     public void 워크스페이스_유저_삭제(){
//         //given
//         User admin = new User("admin@gmail.com", "admin", "test1111", "profile", UserRole.ADMIN);
//         User user1 = new User("test11@gmail.com", "test11", "test1111", "profile", UserRole.USER);
//         User user2 = new User("test22@gmail.com", "test22", "test2222", "profile", UserRole.USER);
//         User user3 = new User("test33@gmail.com", "test33", "test3333", "profile", UserRole.USER);
        
//         Workspace workspace = Workspace.builder()
//                                         .name("testWorkspace1")
//                                         .user(admin)
//                                         .build();
        
//         workspace.addUser(user1);
//         workspace.addUser(user2);
        
//         given(workspaceRepository.findById(1L)).willReturn(Optional.of(workspace));
//         given(workspaceRepository.findById(2L)).willReturn(Optional.ofNullable(null));
//         given(userRepository.findByEmail("admin@gmail.com")).willReturn(Optional.of(admin));
//         given(userRepository.findByEmail("failEmail@gmail.com")).willReturn(Optional.ofNullable(null));
//         given(userRepository.findByEmail("test33@gmail.com")).willReturn(Optional.of(user3));
//         given(userRepository.findById(1L)).willReturn(Optional.of(user1));
//         given(userRepository.findById(2L)).willReturn(Optional.of(user2));
//         given(userRepository.findById(5L)).willReturn(Optional.ofNullable(null));
        
//         //when
//         workspaceService.removeUser(1L, "admin@gmail.com", 2L);
        
//         //then
//         assertEquals(workspace.getName(), "testWorkspace2");
//         assertEquals(workspace.getUserWorkspaces().size(), 2);
//         assertEquals(workspace.getUserWorkspaces().get(0).getUser().getEmail(), "admin@gmail.com");
//         assertEquals(workspace.getUserWorkspaces().get(1).getUser().getEmail(), "test11@gmail.com");
        
//         // 존재하지 않는 workspace id 입력
//         assertThrows(NoSuchWorkspaceException.class, () -> {
//             workspaceService.removeUser(2L, "admin@gmail.com", 2L);
//         });
        
//         // 올바르지 않은 유저 이메일 입력
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.removeUser(1L, "failEmail@gmail.com", 2L);
//         });
        
//         // 올바르지 않은 유저 id로 삭제 요청
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.removeUser(1L, "admin@gmail.com", 5L);
//         });
        
//         // 권한 없는 유저 이메일 입력 (workspace에 속하지 않은 유저)
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.addUser(1L, "test33@gmail.com", 3L);
//         });
//     }
    
//     @Test
//     public void 워크스페이스_유저_권한_변경(){
//         //given
//         User admin = new User("admin@gmail.com", "admin", "test1111", "profile", UserRole.ADMIN);
//         User user1 = new User("test11@gmail.com", "test", "test1111", "profile", UserRole.USER);
//         User user2 = new User("test22@gmail.com", "test22", "test2222", "profile", UserRole.USER);

//         Workspace workspace = Workspace.builder()
//                                         .name("testWorksapce1")
//                                         .user(admin)
//                                         .build();
//         workspace.addUser(user1);
        
//         given(userRepository.findByEmail("admin@gmail.com")).willReturn(Optional.of(admin));
//         given(userRepository.findByEmail("failEmail@gmail.com")).willReturn(Optional.ofNullable(null));
//         given(userRepository.findByEmail("test22@gmail.com")).willReturn(Optional.of(user2));
//         given(userRepository.findById(1L)).willReturn(Optional.of(user1));
//         given(userRepository.findById(2L)).willReturn(Optional.ofNullable(null));
//         given(workspaceRepository.findById(1L)).willReturn(Optional.of(workspace));
//         given(workspaceRepository.findById(2L)).willReturn(Optional.ofNullable(null));
        
//         //when
//         Workspace result = workspaceService.changeUserAuthority(1L, "admin@gmail.com", 1L, UserRole.ADMIN);
        
//         //then
//         assertEquals(result.getUserWorkspaces().get(1).getUserRole(), UserRole.ADMIN);
        
//         // 올바르지 않은 이메일 입력
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.changeUserAuthority(1L, "failEmail@gmail.com", 1L, UserRole.ADMIN);
//         });
        
//         // 올바르지 않은 workspace id 입력
//         assertThrows(NoSuchWorkspaceException.class, () -> {
//             workspaceService.changeUserAuthority(2L, "admin@gmail.com", 1L, UserRole.ADMIN);
//         });
        
//         // 올바르지 않은 user id 입력
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.changeUserAuthority(1L, "admin@gmail.com", 2L, UserRole.ADMIN);
//         });
        
//         // 권한 없는 유저 이메일 입력 (workspace에 속하지 않은 유저)
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.changeUserAuthority(1L, "test22@gmail.com", 1L, UserRole.ADMIN);
//         });
//     }
    
//     @Test
//     public void 캘린더_응답_반환(){
//         //given
//         User admin = new User("admin@gmail.com", "admin", "test1111", "profile", UserRole.ADMIN);
//         User user1 = new User("test11@gmail.com", "test", "test1111", "profile", UserRole.USER);

//         Workspace workspace = Workspace.builder()
//                                         .name("testWorkspace1")
//                                         .user(admin)
//                                         .build();
        
//         LocalDateTime now = LocalDateTime.now();
//         List<Schedule> scheduleList = new ArrayList<>();
        
//         Schedule schedule1 = Schedule.builder()
//                                     .workspace(workspace)
//                                     .name("testSchedule1")
//                                     .startDate(now)
//                                     .endDate(now)
//                                     .content("test")
//                                     .users(new ArrayList<User>())
//                                     .build();
        
//         Schedule schedule2 = Schedule.builder()
//                                     .workspace(workspace)
//                                     .name("testSchedule2")
//                                     .startDate(now)
//                                     .endDate(now)
//                                     .content("test")
//                                     .users(new ArrayList<User>())
//                                     .build();
//         scheduleList.add(schedule1);
//         scheduleList.add(schedule2);
        
//         given(userRepository.findByEmail("admin@gmail.com")).willReturn(Optional.of(admin));
//         given(userRepository.findByEmail("failEmail@gmail.com")).willReturn(Optional.ofNullable(null));
//         given(userRepository.findByEmail("test11@gmail.com")).willReturn(Optional.of(user1));
//         given(workspaceRepository.findById(1L)).willReturn(Optional.of(workspace));
//         given(workspaceRepository.findById(2L)).willReturn(Optional.ofNullable(null));
//         given(scheduleRepository.searchSchedule(any(Workspace.class), any(LocalDateTime.class), any(LocalDateTime.class))).willReturn(scheduleList);
        
//         //when
//         CalendarResponse result = workspaceService.getCalendar(1L, "admin@gmail.com", now);
        
//         //then
//         assertEquals(result.getWorkspaceName(), "testWorkspace1");
//         assertEquals(result.getSchedules().get(0).getScheduleName(), "testSchedule1");
//         assertEquals(result.getSchedules().get(1).getScheduleName(), "testSchedule2");
        
//         // 올바르지 않은 이메일 입력
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.getCalendar(1L, "failEmail@gmail.com", now);
//         });
        
//         // 올바르지 않은 workspace id 입력
//         assertThrows(NoSuchWorkspaceException.class, () -> {
//             workspaceService.getCalendar(2L, "admin@gmail.com", now);
//         });
        
//         // 권한 없는 유저 이메일 입력 (workspace에 속하지 않은 유저)
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.getCalendar(1L, "test11@gmail.com", now);
//         });
//     }
    
//     @Test
//     public void 오늘의_일정_반환(){
//         //given
//         User admin = new User("admin@gmail.com", "admin", "test1111", "profile", UserRole.ADMIN);
//         User user1 = new User("test11@gmail.com", "test", "test1111", "profile", UserRole.USER);

//         Workspace workspace = Workspace.builder()
//                                         .name("testWorkspace1")
//                                         .user(admin)
//                                         .build();
        
//         LocalDateTime now = LocalDateTime.now();
//         List<Schedule> scheduleList = new ArrayList<>();
        
//         Schedule schedule1 = Schedule.builder()
//                                     .workspace(workspace)
//                                     .name("testSchedule1")
//                                     .startDate(now)
//                                     .endDate(now)
//                                     .content("test")
//                                     .users(new ArrayList<User>())
//                                     .build();
        
//         Schedule schedule2 = Schedule.builder()
//                                     .workspace(workspace)
//                                     .name("testSchedule2")
//                                     .startDate(now)
//                                     .endDate(now)
//                                     .content("test")
//                                     .users(new ArrayList<User>())
//                                     .build();
//         scheduleList.add(schedule1);
//         scheduleList.add(schedule2);
        
//         given(userRepository.findByEmail("admin@gmail.com")).willReturn(Optional.of(admin));
//         given(userRepository.findByEmail("failEmail@gmail.com")).willReturn(Optional.ofNullable(null));
//         given(userRepository.findByEmail("test11@gmail.com")).willReturn(Optional.of(user1));
//         given(workspaceRepository.findById(1L)).willReturn(Optional.of(workspace));
//         given(workspaceRepository.findById(2L)).willReturn(Optional.ofNullable(null));
//         given(scheduleRepository.searchSchedule(any(Workspace.class), any(LocalDateTime.class), any(LocalDateTime.class))).willReturn(scheduleList);
        
//         //when
//         CalendarResponse result = workspaceService.getDailySchedules(1L, "admin@gmail.com", now);
        
//         //then
//         assertEquals(result.getWorkspaceName(), "testWorkspace1");
//         assertEquals(result.getSchedules().get(0).getScheduleName(), "testSchedule1");
//         assertEquals(result.getSchedules().get(1).getScheduleName(), "testSchedule2");
        
//         // 올바르지 않은 이메일 입력
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.getDailySchedules(1L, "failEmail@gmail.com", now);
//         });
        
//         // 올바르지 않은 workspace id 입력
//         assertThrows(NoSuchWorkspaceException.class, () -> {
//             workspaceService.getDailySchedules(2L, "admin@gmail.com", now);
//         });
        
//         // 권한 없는 유저 이메일 입력 (workspace에 속하지 않은 유저)
//         assertThrows(NoSuchUserException.class, () -> {
//             workspaceService.getDailySchedules(1L, "test11@gmail.com", now);
//         });
//     }
// }