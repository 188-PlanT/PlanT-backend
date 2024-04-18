// package project.service;

// import static org.mockito.BDDMockito.*;
// import static org.mockito.ArgumentMatchers.any;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.junit.runner.RunWith;
// import org.mockito.junit.MockitoJUnitRunner;
// import org.junit.Test;
// import static org.assertj.core.api.Assertions.assertThat;

// import project.domain.user.dao.UserRepository;
// import project.domain.schedule.dao.ScheduleRepository;
// import project.domain.schedule.dao.DevLogRepository;
// import project.domain.*;
// import project.dto.devLog.*;
// import project.exception.user.*;
// import project.exception.devLog.*;
// import project.exception.schedule.*;

// import java.util.Optional;
// import java.util.List;
// import java.util.ArrayList;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.PageRequest;

// @RunWith(MockitoJUnitRunner.class)
// public class DevLogServiceTest{
    
//     @Mock
//     private ScheduleRepository scheduleRepository;
    
//     @Mock
//     private UserRepository userRepository;
    
//     @Mock
//     private DevLogRepository devLogRepository;
    
//     @InjectMocks
//     private DevLogServiceImpl devLogService;
    
//     //스케줄 생성 메소드
//     private Schedule getSchedule(User user){
//         List<User> userList = new ArrayList<>();
//         userList.add(user);
        
//         Workspace workspace = new Workspace("testWorkspace1", userList);

//         return new Schedule(workspace, "testSchedule1", userList);
//     }
    
//     @Test
//     public void 개발일지_생성(){
//         //given
//         User user = new User("test1", "test1234!", "kim");
//         String accountId = user.getAccountId();
//         Schedule schedule = getSchedule(user);
//         Long scheduleId = 1L;
        
//         DevLog devLog = new DevLog(schedule, user, "testDevLog");
        
//         CreateDevLogRequest request = new CreateDevLogRequest(scheduleId, accountId , "testDevLog");
        
//         given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));
//         given(userRepository.findByAccountId(accountId)).willReturn(Optional.of(user));
//         given(devLogRepository.existsByScheduleAndUser(schedule, user)).willReturn(false);
//         given(devLogRepository.save(any(DevLog.class))).willReturn(devLog);
        
//         //when
//         Long devLogId = devLogService.createDevLog(request);
//     }
    
//     @Test(expected = NoSuchScheduleException.class)
//     public void 없는_스케줄_개발일지_생성(){
//         //given
//         Long undefinedId = 1L;
        
//         CreateDevLogRequest request = new CreateDevLogRequest(undefinedId, "test1", "testDevLog");
        
//         given(scheduleRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));
        
//         //when
//         Long devLogId = devLogService.createDevLog(request);
//     }
    
//     @Test(expected = NoSuchUserException.class)
//     public void 없는_유저_개발일지_생성(){
//         //given
//         User user = new User("test1", "test1234!", "kim");
//         String undefinedAccountId = "test2";
//         Schedule schedule = getSchedule(user);
//         Long scheduleId = 1L;
        
//         CreateDevLogRequest request = new CreateDevLogRequest(scheduleId, undefinedAccountId , "testDevLog");
        
//         given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));
//         given(userRepository.findByAccountId(undefinedAccountId)).willReturn(Optional.ofNullable(null));
    
//         //when
//         Long devLogId = devLogService.createDevLog(request);
//     }
    
//     @Test(expected = IllegalStateException.class)
//     public void 개발일지_중복_생성(){
//         //given
//         User user = new User("test1", "test1234!", "kim");
//         String accountId = user.getAccountId();
//         Schedule schedule = getSchedule(user);
//         Long scheduleId = 1L;
        
//         CreateDevLogRequest request = new CreateDevLogRequest(scheduleId, accountId , "testDevLog");
        
//         given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));
//         given(userRepository.findByAccountId(accountId)).willReturn(Optional.of(user));
//         given(devLogRepository.existsByScheduleAndUser(schedule, user)).willReturn(true);
        
//         //when
//         Long devLogId = devLogService.createDevLog(request);
//     }
    
    
//     @Test
//     public void 스케줄_전체_조회(){
//         //given
//         User user1 = new User("test1", "test1234!", "kim");
//         User user2 = new User("test2", "test1234!", "park"); 
//         Schedule schedule1 = getSchedule(user1);
//         Schedule schedule2 = getSchedule(user2);
        
//         List<DevLog> devLogList = new ArrayList<>();
//         devLogList.add(new DevLog(schedule1, user1, "testDevLog1"));
//         devLogList.add(new DevLog(schedule1, user2, "testDevLog2"));
//         devLogList.add(new DevLog(schedule2, user1, "testDevLog3"));
//         devLogList.add(new DevLog(schedule2, user2, "testDevLog4"));
        
//         Pageable pageable = PageRequest.of(0,20);
        
//         Page<DevLog> response = new PageImpl<>(devLogList, pageable, 4);
        
//         // given(scheduleRepository.findById(any(Long.class)).willReturn(Optional.of(schedule));
//         // given(userRepository.findByAccountId(any(String.class)).willReturn(Optional.of(user));
//         given(devLogRepository.searchDevLogs(any(Pageable.class), any(), any()))
//               .willReturn(response);
        
//         //when
//         List<DevLog> result = devLogService.findAllBySearch(pageable, null, null).getContent();
        
//         //then
//         assertThat(result.size()).isEqualTo(4);
//         assertThat(result.get(0).getContent()).isEqualTo("testDevLog1");
//         assertThat(result.get(1).getContent()).isEqualTo("testDevLog2");
//         assertThat(result.get(2).getContent()).isEqualTo("testDevLog3");
//         assertThat(result.get(3).getContent()).isEqualTo("testDevLog4");
//     }
    
//     // @Test(expected = NoSuchScheduleException.class)
//     // public void 없는_스케줄_조회() throws Exception{
//     //     //given
//     //     Long undefinedId = 1L;
        
//     //     given(scheduleRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));
        
//     //     //when
//     //     Schedule result = scheduleService.findOne(undefinedId);
//     // }
    
//     // @Test
//     // public void 스케줄_리스트_조회(){
//     //     //given
//     //     Workspace workspace = new Workspace("testWorkspace1",getUserList());
        
//     //     List<Schedule> scheduleList = new ArrayList<>();
//     //     scheduleList.add(new Schedule(workspace, "testSchedule1", getUserList()));
//     //     scheduleList.add(new Schedule(workspace, "testSchedule2", getUserList()));
//     //     scheduleList.add(new Schedule(workspace, "testSchedule3", getUserList()));
        
//     //     Pageable pageable = PageRequest.of(0,20);
        
//     //     Page<Schedule> response = new PageImpl<>(scheduleList, pageable, 3);
        
//     //     given(scheduleRepository.findAll(any(Pageable.class))).willReturn(response);
        
//     //     //when
//     //     List<Schedule> result = scheduleService.findSchedules(pageable).getContent();
        
//     //     //then
//     //     assertThat(result.size()).isEqualTo(3);
//     //     assertThat(result.get(0).getName()).isEqualTo("testSchedule1");
//     //     assertThat(result.get(1).getName()).isEqualTo("testSchedule2");
//     //     assertThat(result.get(2).getName()).isEqualTo("testSchedule3");
//     // }
    
//     // @Test(expected = NoSuchWorkspaceException.class)
//     // public void 없는_워크스페이스에_스케줄_생성(){
//     //     //given
//     //     List<String> userNameList = new ArrayList<>();
        
//     //     CreateScheduleRequest request = new CreateScheduleRequest("testWorkspace2", "testSchedule1", userNameList);
        
//     //     given(workspaceRepository.findByName("testWorkspace2")).willReturn(Optional.ofNullable(null));
        
//     //     //when
//     //     Long scheduleId = scheduleService.createSchedule(request);
//     // }
    
//     // @Test
//     // public void 스케줄_수정(){
//     //     //given
//     //     List<User> userList = getUserList();
        
//     //     List<String> userAccountIdList = new ArrayList<>();
//     //     userAccountIdList.add("test1");
//     //     userAccountIdList.add("test2");
        
//     //     List<User> updateUserList = new ArrayList<>();
//     //     updateUserList.add(userList.get(0));
//     //     updateUserList.add(userList.get(1));
        
//     //     Workspace workspace = new Workspace("testWorkspace1",userList);
        
//     //     Schedule schedule = new Schedule(workspace, "testSchedule2", userList);
//     //     Long scheduleId = 1L;
        
//     //     UpdateScheduleRequest request = new UpdateScheduleRequest("testSchedule2", userAccountIdList);
        
        
//     //     given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));
//     //     given(userRepository.findUsersByAccounIdList(any(List.class))).willReturn(updateUserList);
//     //     //when
//     //     Schedule result = scheduleService.updateSchedule(scheduleId,request);
        
//     //     //then
//     //     assertThat(result.getName()).isEqualTo("testSchedule2");
//     //     assertThat(result.getWorkspace().getName()).isEqualTo("testWorkspace1");
//     //     assertThat(result.getUserSchedules().size()).isEqualTo(2);
//     //     assertThat(result.getUserSchedules().get(0).getUser().getAccountId()).isEqualTo("test1");
//     //     assertThat(result.getUserSchedules().get(1).getUser().getAccountId()).isEqualTo("test2");
//     // }
    
//     // @Test(expected = NoSuchScheduleException.class)
//     // public void 없는_스케줄_수정(){
//     //     //given
//     //     List<String> userAccountIdList = new ArrayList<>();
//     //     UpdateScheduleRequest request = new UpdateScheduleRequest("testSchedule2", userAccountIdList);
        
//     //     Long undefinedId = 1L;
        
//     //      given(scheduleRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));
        
//     //     //when
//     //     Schedule result = scheduleService.updateSchedule(undefinedId,request);
//     // }
    
//     // @Test
//     // public void 스케줄_삭제(){
//     //     //given
//     //     Workspace workspace = new Workspace("testWorkspace1",getUserList());
//     //     Schedule schedule = new Schedule(workspace, "testSchedule1", getUserList());
//     //     Long scheduleId = 1L;
        
//     //     given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));
        
//     //     //when
//     //     scheduleService.removeSchedule(scheduleId);
//     // }
    
//     // @Test(expected = NoSuchScheduleException.class)
//     // public void 없는_스케줄_삭제() throws Exception{
//     //     //given
//     //     Workspace workspace = new Workspace("testWorkspace1",getUserList());
//     //     Schedule schedule = new Schedule(workspace, "testSchedule1", getUserList());
//     //     Long undefinedId = 1L;
        
//     //     given(scheduleRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));
        
//     //     //when
//     //     scheduleService.removeSchedule(undefinedId);
//     // }
    
//     // @Test
//     // public void 스케줄_유저_추가(){
//     //     //given
//     //     Workspace workspace = new Workspace("testWorkspace1",getUserList());
//     //     Schedule schedule = new Schedule(workspace, "testSchedule1", getUserList());
//     //     Long scheduleId = 1L;
        
//     //     User user = new User("test4", "test1234!", "kim");
//     //     String accountId = "test4";
        
//     //     given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));
//     //     given(userRepository.findByAccountId(accountId)).willReturn(Optional.of(user));        
        
//     //     //when
//     //     Schedule result = scheduleService.addUser(scheduleId, accountId);
        
//     //     //then
//     //     assertThat(result.getName()).isEqualTo("testSchedule1");
//     //     assertThat(result.getWorkspace().getName()).isEqualTo("testWorkspace1");
//     //     assertThat(result.getUserSchedules().size()).isEqualTo(4);
//     //     assertThat(result.getUserSchedules().get(0).getUser().getAccountId()).isEqualTo("test1");
//     //     assertThat(result.getUserSchedules().get(1).getUser().getAccountId()).isEqualTo("test2");
//     //     assertThat(result.getUserSchedules().get(2).getUser().getAccountId()).isEqualTo("test3");
//     //     assertThat(result.getUserSchedules().get(3).getUser().getAccountId()).isEqualTo("test4");
//     // }
    
//     // @Test(expected = NoSuchScheduleException.class)
//     // public void 없는_스케줄_유저_추가(){
//     //     //given
//     //     Long undefinedId = 1L;
        
//     //     String accountId = "test4";
        
//     //     given(scheduleRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));      
        
//     //     //when
//     //     Schedule result = scheduleService.addUser(undefinedId, accountId);
//     // }
    
//     // @Test(expected = NoSuchUserException.class)
//     // public void 스케줄_없는_유저_추가(){
//     //     //given
//     //     Workspace workspace = new Workspace("testWorkspace1",getUserList());
//     //     Schedule schedule = new Schedule(workspace, "testSchedule1", getUserList());
//     //     Long scheduleId = 1L;
        
//     //     String undefinedAccountId = "test4";
        
//     //     given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));      
//     //     given(userRepository.findByAccountId(undefinedAccountId)).willReturn(Optional.ofNullable(null));  
        
//     //     //when
//     //     Schedule result = scheduleService.addUser(scheduleId, undefinedAccountId);
//     // }
    
//     // @Test(expected = IllegalStateException.class)
//     // public void 스케줄_중복_유저_추가(){
//     //     //given
//     //     List<User> userList = getUserList();
        
//     //     Workspace workspace = new Workspace("testWorkspace1", userList);
//     //     Schedule schedule = new Schedule(workspace, "testSchedule1", userList);
//     //     Long scheduleId = 1L;
        
//     //     User user = userList.get(2);
//     //     String accountId = "test3";
        
//     //     given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));
//     //     given(userRepository.findByAccountId(accountId)).willReturn(Optional.of(user)); 
        
//     //     //when
//     //     Schedule result = scheduleService.addUser(scheduleId, accountId);
//     // }
    
//     // @Test
//     // public void 스케줄_유저_삭제(){
//     //     //given
//     //     List<User> userList = getUserList();
        
//     //     Workspace workspace = new Workspace("testWorkspace1", userList);
//     //     Schedule schedule = new Schedule(workspace, "testSchedule1", userList);
//     //     Long scheduleId = 1L;
        
//     //     User user = userList.get(2);
//     //     Long userId = 2L;
        
//     //     given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));  
//     //     given(userRepository.findById(userId)).willReturn(Optional.of(user));        
        
//     //     //when
//     //     scheduleService.removeUser(scheduleId, userId);
        
//     //     //then
//     //     assertThat(schedule.getName()).isEqualTo("testSchedule1");
//     //     assertThat(schedule.getWorkspace().getName()).isEqualTo("testWorkspace1");
//     //     assertThat(schedule.getUserSchedules().size()).isEqualTo(2);
//     //     assertThat(schedule.getUserSchedules().get(0).getUser().getAccountId()).isEqualTo("test1");
//     //     assertThat(schedule.getUserSchedules().get(1).getUser().getAccountId()).isEqualTo("test2");
//     // }
    
//     // @Test(expected = NoSuchScheduleException.class)
//     // public void 없는_스케줄_유저_삭제(){
//     //     //given
//     //     Long undefinedId = 1L;
        
//     //     Long userId = 2L;
        
//     //     given(scheduleRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));       
        
//     //     //when
//     //     scheduleService.removeUser(undefinedId, userId);
//     // }
    
//     // @Test(expected = NoSuchUserException.class)
//     // public void 스케줄_없는_유저_삭제(){
//     //     //given
//     //     Workspace workspace = new Workspace("testWorkspace1", getUserList());
//     //     Schedule schedule = new Schedule(workspace, "testSchedule1", getUserList());
//     //     Long scheduleId = 1L;
        
//     //     Long undefinedUserId = 2L;
        
//     //     given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));     
//     //     given(userRepository.findById(undefinedUserId)).willReturn(Optional.ofNullable(null));  
        
//     //     //when
//     //     scheduleService.removeUser(scheduleId, undefinedUserId);
//     // }
    
//     // @Test(expected = IllegalStateException.class)
//     // public void 스케줄_미참여_유저_삭제(){
//     //     //given
//     //     List<User> userList = getUserList();
        
//     //     Workspace workspace = new Workspace("testWorkspace1", userList);
//     //     Schedule schedule = new Schedule(workspace, "testSchedule1", userList);
//     //     Long scheduleId = 1L;
        
//     //     User user = new User("test4", "test1234!", "kim");
//     //     Long userId = 4L;
        
//     //     given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));  
//     //     given(userRepository.findById(userId)).willReturn(Optional.of(user)); 
//     //     //when
//     //     scheduleService.removeUser(scheduleId, userId);
//     // }
// }