// package project.service;

// import project.repository.UserRepository;
// import project.repository.UserWorkspaceRepository;
// import project.repository.UserScheduleRepository;
// import project.domain.*;
// import project.exception.user.*;
// import project.exception.auth.InvalidCodeException;
// import project.dto.user.*;
// import project.dto.login.*;
// import org.springframework.security.crypto.password.PasswordEncoder;

// import java.util.ArrayList;
// import java.util.Optional;
// import java.util.List;
// import java.time.LocalDateTime;

// import org.junit.jupiter.api.*;
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
// public class UserServiceTest{
    
//     @Mock
//     private UserRepository userRepository;
    
//     @Mock
//     private UserWorkspaceRepository userWorkspaceRepository;
    
//     @Mock
//     private UserScheduleRepository userScheduleRepository;
    
//     @Mock
//     private PasswordEncoder passwordEncoder;
    
//     @Mock
//     private RedisService redisService;
    
//     @InjectMocks
//     private UserService userService;
    
//     @Test
//     public void 회원가입(){
//         //given
//         String successEmail = "test1234@gmail.com";
//         String failEmail = "alreadyExist1234@gmail.com";
        
//         SignUpRequest successRequest = new SignUpRequest(successEmail, "test1234");
//         SignUpRequest failRequest = new SignUpRequest(failEmail, "test1234");
        
//         given(userRepository.existsByEmail(successEmail)).willReturn(false); // 가입 가능 이메일
//         given(userRepository.existsByEmail(failEmail)).willReturn(true); // 중복 이메일
//         given(passwordEncoder.encode(any(String.class))).willReturn("encodedPassword"); //Entity 의존성 줄여볼까??
        
//         //when
//         User result = userService.register(successRequest);
        
//         //then
//         assertEquals(result.getEmail(), successEmail);
//         assertEquals(result.getPassword(), "encodedPassword");
//         assertNull(result.getNickName());
//         assertEquals(result.getUserRole(), UserRole.PENDING);
        
//         assertThrows(UserAlreadyExistException.class, () -> {
//             userService.register(failRequest);
//         });
//     }
//     @Test
//     public void 회원가입_마무리(){
//         //given
//         String successEmail = "test1234@gmail.com";
//         String successNickName = "test1234";
        
//         String failEmail = "UnExistEmail1234@gmail.com";
//         String failNickName = "alreadyExistNickName";
        
//         given(passwordEncoder.encode(any(String.class))).willReturn("encodedPassword"); //Entity 의존성 줄여볼까??
//         User user = User.ofEmailPassword(successEmail, "test1234", passwordEncoder);

//         given(userRepository.findByEmail(successEmail)).willReturn(Optional.of(user)); // 가입된 이메일
//         given(userRepository.findByEmail(failEmail)).willReturn(Optional.ofNullable(null)); // 가입안된 이메일

//         given(userRepository.existsByNickName(successNickName)).willReturn(false); // 가입 가능 닉네임
//         given(userRepository.existsByNickName(failNickName)).willReturn(true); // 중복 닉네임
        
//         //when
//         User result = userService.finishRegister(successEmail, successNickName);
        
//         //then
//         assertEquals(result.getEmail(), successEmail);
//         assertEquals(result.getPassword(), "encodedPassword");
//         assertEquals(result.getNickName(), successNickName);
//         assertEquals(result.getUserRole(), UserRole.USER);
        
//         assertThrows(NoSuchUserException.class, () -> {
//             userService.finishRegister(failEmail, successNickName);
//         });
//         assertThrows(UserAlreadyExistException.class, () -> {
//             userService.finishRegister(successEmail, failNickName);
//         });
//     }
    
//     @Test
//     public void 유저_로그인(){
//         //given
//         String successEmail = "test1234@gmail.com";
//         String successPassword = "test1234";
        
//         String failEmail = "UnExistEmail1234@gmail.com";
//         String failPassword = "fail1234";
        
//         given(passwordEncoder.encode(successPassword)).willReturn(successPassword);
//         given(passwordEncoder.matches(successPassword, successPassword)).willReturn(true);
        
//         User user = User.ofEmailPassword(successEmail, successPassword, passwordEncoder);

//         given(userRepository.findByEmail(successEmail)).willReturn(Optional.of(user)); // 가입된 이메일
//         given(userRepository.findByEmail(failEmail)).willReturn(Optional.ofNullable(null)); // 가입안된 이메일
        
//         //when
//         User result = userService.signIn(successEmail, successPassword);
        
//         //then
//         assertEquals(result.getEmail(), successEmail);
//         assertEquals(result.getPassword(), successPassword);
        
//         assertThrows(NoSuchUserException.class, () -> {
//             userService.signIn(successEmail, failPassword);
//         });
//         assertThrows(NoSuchUserException.class, () -> {
//             userService.signIn(failEmail, successPassword);
//         });

//     }
    
//     @Test
//     public void 워크스페이스_조회(){
//         //given
//         List<UserWorkspace> userWorkspaceList = new ArrayList<>();
        
//         User user = User.ofEmailPassword("test11@gmail.com", "test1111", passwordEncoder);
        
//         Workspace workspace1 = Workspace.builder()
//                                         .name("testWorkspace1")
//                                         .user(user)
//                                         .build();
        
//         Workspace workspace2 = Workspace.builder()
//                                         .name("testWorkspace2")
//                                         .user(user)
//                                         .build();
        
//         Workspace workspace3 = Workspace.builder()
//                                         .name("testWorkspace3")
//                                         .user(user)
//                                         .build();
        
        
//         userWorkspaceList.add(new UserWorkspace(user, workspace1, UserRole.ADMIN));
//         userWorkspaceList.add(new UserWorkspace(user, workspace2, UserRole.USER));
//         userWorkspaceList.add(new UserWorkspace(user, workspace3, UserRole.PENDING));
        
        
//         Pageable pageable = PageRequest.of(0,20);
//         Page<UserWorkspace> response = new PageImpl<>(userWorkspaceList, pageable, 3);

//         given(userRepository.findByEmail("test11@gmail.com")).willReturn(Optional.of(user));
//         given(userRepository.findByEmail("failEmail@gmail.com")).willReturn(Optional.ofNullable(null));
//         given(userWorkspaceRepository.searchByUser(user, pageable)).willReturn(response);
        
//         // //when
//         Page<UserWorkspace> result = userService.findWorkspaces("test11@gmail.com", pageable);
        
//         //then
//         assertEquals(result.getContent().size(), 3);
//         assertEquals(result.getContent().get(0).getWorkspace().getName(), "testWorkspace1");
//         assertEquals(result.getContent().get(0).getUserRole(), UserRole.ADMIN);
        
//         assertEquals(result.getContent().get(1).getWorkspace().getName(), "testWorkspace2");
//         assertEquals(result.getContent().get(1).getUserRole(), UserRole.USER);
        
//         assertEquals(result.getContent().get(2).getWorkspace().getName(), "testWorkspace3");
//         assertEquals(result.getContent().get(2).getUserRole(), UserRole.PENDING);
        
//         assertThrows(NoSuchUserException.class, () -> {
//             userService.findWorkspaces("failEmail@gmail.com", pageable);
//         });
//     }
    
//     @Test
//     public void 스케줄_조회(){
//         //given
//         List<UserSchedule> userScheduleList = new ArrayList<>();
//         List<User> userList = new ArrayList<>();
        
//         User user = User.ofEmailPassword("test11@gmail.com", "test1111", passwordEncoder);
//         userList.add(user);
        
//         Workspace workspace = Workspace.builder()
//                                         .name("testWorkspace1")
//                                         .user(user)
//                                         .build();
        
        
//         LocalDateTime now = LocalDateTime.now();
        
//         Schedule schedule1 = Schedule.builder()
//                                     .workspace(workspace)
//                                     .name("testSchedule1")
//                                     .startDate(now)
//                                     .endDate(now)
//                                     .content("test")
//                                     .users(userList)
//                                     .build();
        
//         Schedule schedule2 = Schedule.builder()
//                                     .workspace(workspace)
//                                     .name("testSchedule2")
//                                     .startDate(now)
//                                     .endDate(now)
//                                     .content("test")
//                                     .users(userList)
//                                     .build();
        
//         userScheduleList.add(new UserSchedule(user,schedule1));
//         userScheduleList.add(new UserSchedule(user,schedule2));
        
//         Pageable pageable = PageRequest.of(0,20);
//         Page<UserSchedule> response = new PageImpl<>(userScheduleList, pageable, 2);

//         given(userRepository.findByEmail("test11@gmail.com")).willReturn(Optional.of(user));
//         given(userRepository.findByEmail("failEmail@gmail.com")).willReturn(Optional.ofNullable(null));
//         given(userScheduleRepository.searchByUser(user, now, pageable)).willReturn(response);
        
//         //when
//         Page<UserSchedule> result = userService.findSchedules("test11@gmail.com", now, pageable);
        
//         //then
//         assertEquals(result.getContent().size(), 2);
//         assertEquals(result.getContent().get(0).getSchedule().getName(), "testSchedule1");
//         assertEquals(result.getContent().get(1).getSchedule().getName(), "testSchedule2");
        
//         assertThrows(NoSuchUserException.class, () -> {
//             userService.findSchedules("failEmail@gmail.com", now, pageable);
//         });
//     }
    
//     @Test
//     public void 유저정보_수정(){
//         //given
//         given(passwordEncoder.encode(any(String.class))).willReturn("encodedPassword"); //Entity 의존성 줄여볼까??
        
//         User user1 = User.ofEmailPassword("test11@gmail.com", "test1111", passwordEncoder);
//         user1.setNickName("test11");

//         User user2 = User.ofEmailPassword("test22@gmail.com", "test2222", passwordEncoder);
//         user2.setNickName("test22");
        
//         given(userRepository.findByEmail("test11@gmail.com")).willReturn(Optional.of(user1));
//         given(userRepository.findByEmail("test22@gmail.com")).willReturn(Optional.of(user2));
//         given(userRepository.findByEmail("failEmail@gmail.com")).willReturn(Optional.ofNullable(null));
//         given(userRepository.existsByNickName("update11")).willReturn(false);
        
//         //when
//         User updatedUser1 = userService.updateUser("test11@gmail.com", "update11", "test1111", "profile1");
//         User updatedUser2 = userService.updateUser("test22@gmail.com", "test22", "test2222", "profile2");
        
//         //then
//         assertEquals(updatedUser1.getNickName(), "update11");
//         assertEquals(updatedUser1.getPassword(), "encodedPassword");
//         assertEquals(updatedUser1.getProfile(), "profile1");
        
//         assertEquals(updatedUser2.getNickName(), "test22");
//         assertEquals(updatedUser2.getPassword(), "encodedPassword");
//         assertEquals(updatedUser2.getProfile(), "profile2");

//         assertThrows(NoSuchUserException.class, () -> {
//             userService.updateUser("failEmail@gmail.com", "test22", "test2222", "profile2");
//         });
//     }

//     @Test
//     public void 유저_검색(){
//         //given
//         User user = User.ofEmailPassword("test11@gmail.com", "test1111", passwordEncoder);
        
//         given(userRepository.searchByKeyword("test11@gmail.com")).willReturn(Optional.of(user));
//         given(userRepository.searchByKeyword("test11")).willReturn(Optional.of(user));
//         given(userRepository.searchByKeyword("failKeyword")).willReturn(Optional.ofNullable(null));
        
//         //when
//         User result1 = userService.searchUser("test11@gmail.com");
//         User result2 = userService.searchUser("test11");
        
//         //then
//         assertEquals(result1.getEmail(), "test11@gmail.com");
//         assertEquals(result2.getEmail(), "test11@gmail.com");
        
//         assertThrows(NoSuchUserException.class, () -> {
//             userService.searchUser("failKeyword");
//         });
//     }
    
//     @Test
//     public void 이메일_검증코드_제작(){
//         //given
//         given(userRepository.existsByEmail("test11@gmail.com")).willReturn(false);
//         given(userRepository.existsByEmail("failEmail@gmail.com")).willReturn(true);
        
//         // //when
//         int code = userService.getEmailValidateCode("test11@gmail.com");
        
//         // //then
//         assertTrue(code < 1000000 && code > 99999);
//         assertThrows(UserAlreadyExistException.class, () -> {
//             userService.getEmailValidateCode("failEmail@gmail.com");
//         });
//     }
    
//     @Test
//     public void 이메일_검증코드_검증(){
//         //given
//         given(redisService.getValues("test11@gmail.com")).willReturn("123456");
//         given(redisService.getValues("failEmail@gmail.com")).willReturn(null);
        
//         // //when
//         userService.validateEmailCode("test11@gmail.com", 123456);
        
//         // //then
//         assertThrows(NoSuchUserException.class, () -> {
//             userService.validateEmailCode("failEmail@gmail.com", 123456);
//         });
//         assertThrows(InvalidCodeException.class, () -> {
//             userService.validateEmailCode("test11@gmail.com", 654321);
//         });
//     }
    
//     // validateEmail, NickName, findByEmail 등의 메서드는 위의 메서드의 내부 메서드로 사용되며 검증되었음
//     // + 단순 호출 및 예외처리 로직이므로 생략함
// }