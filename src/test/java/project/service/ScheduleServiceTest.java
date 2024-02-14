package project.service;

import project.repository.UserRepository;
import project.repository.WorkspaceRepository;
import project.repository.ScheduleRepository;
import project.domain.*;
import project.dto.schedule.*;
import project.exception.user.*;
import project.exception.workspace.*;
import project.exception.schedule.*;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest{
    
    @Mock
    private ScheduleRepository scheduleRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private WorkspaceRepository workspaceRepository;
    
    @InjectMocks
    private ScheduleService scheduleService;
    
    // // 유저 생성 메소드
    // private List<User> getUserList(){
    //     List<User> userList = new ArrayList<>();
        
    //     userList.add(new User("test1", "test1234", "kim"));
    //     userList.add(new User("test2", "test1234", "park"));
    //     userList.add(new User("test3", "test1234", "lee"));
                              
    //     return userList;
    // }
    
    @Test
    public void 스케줄_단일_조회(){
        //given
        User admin = new User("admin@gmail.com", "admin", "test1111", "profile", UserRole.ADMIN);
        User user1 = new User("test11@gmail.com", "test11", "test1111", "profile", UserRole.USER);
        User user2 = new User("test22@gmail.com", "test22", "test2222", "profile", UserRole.USER);
        
        List<User> userList = new ArrayList<>(Arrays.asList(admin, user1));
        
        Workspace workspace = Workspace.builder()
                                        .name("testWorkspace1")
                                        .user(admin)
                                        .build();
        workspace.addUser(user1);
        
        LocalDateTime now = LocalDateTime.now();
        
        Schedule schedule = Schedule.builder()
                                    .workspace(workspace)
                                    .name("testSchedule1")
                                    .startDate(now)
                                    .endDate(now)
                                    .content("test")
                                    .users(userList)
                                    .build();
        
        schedule.addChat(admin, "admin chat");
        schedule.addChat(user1, "user1 chat");
        
        given(scheduleRepository.findById(1L)).willReturn(Optional.of(schedule));
        // given(scheduleRepository.findById(2L)).willReturn(Optional.of(schedule));
        
        //when
        ScheduleDto result = scheduleService.findOne(1L);
        
        //then
        assertEquals(result.getName(), "testSchedule1");
        assertEquals(result.getWorkspaceName(), "testWorkspace1");
        assertEquals(result.getUsers().get(0).getNickName(), "admin");
        assertEquals(result.getUsers().get(1).getNickName(), "test11");
        assertEquals(result.getChatList().get(0).getNickName(), "admin");
        assertEquals(result.getChatList().get(0).getContent(), "admin chat");
        assertEquals(result.getChatList().get(1).getNickName(), "test11");
        assertEquals(result.getChatList().get(1).getContent(), "user1 chat");
    }
    
    // @Test(expected = NoSuchScheduleException.class)
    // public void 없는_스케줄_조회() throws Exception{
    //     //given
    //     Long undefinedId = 1L;
        
    //     given(scheduleRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));
        
    //     //when
    //     Schedule result = scheduleService.findOne(undefinedId);
    // }
    
    // @Test
    // public void 스케줄_리스트_조회(){
    //     //given
    //     Workspace workspace = new Workspace("testWorkspace1",getUserList());
        
    //     List<Schedule> scheduleList = new ArrayList<>();
    //     scheduleList.add(new Schedule(workspace, "testSchedule1", getUserList()));
    //     scheduleList.add(new Schedule(workspace, "testSchedule2", getUserList()));
    //     scheduleList.add(new Schedule(workspace, "testSchedule3", getUserList()));
        
    //     Pageable pageable = PageRequest.of(0,20);
        
    //     Page<Schedule> response = new PageImpl<>(scheduleList, pageable, 3);
        
    //     given(scheduleRepository.findAll(any(Pageable.class))).willReturn(response);
        
    //     //when
    //     List<Schedule> result = scheduleService.findSchedules(pageable).getContent();
        
    //     //then
    //     assertThat(result.size()).isEqualTo(3);
    //     assertThat(result.get(0).getName()).isEqualTo("testSchedule1");
    //     assertThat(result.get(1).getName()).isEqualTo("testSchedule2");
    //     assertThat(result.get(2).getName()).isEqualTo("testSchedule3");
    // }
    
    // @Test
    // public void 스케줄_생성(){
    //     //given
    //     List<String> userNameList = new ArrayList<>();
    //     userNameList.add("test1");
    //     userNameList.add("test2");
    //     userNameList.add("test3");
        
    //     Workspace workspace = new Workspace("testWorkspace1",getUserList());
        
    //     Schedule schedule = new Schedule(workspace, "testSchedule1", getUserList());
        
    //     CreateScheduleRequest request = new CreateScheduleRequest("testWorkspace1", "testSchedule1", userNameList);
        
    //     given(workspaceRepository.findByName("testWorkspace1")).willReturn(Optional.of(workspace));
    //     given(userRepository.findUsersByAccounIdList(any(List.class))).willReturn(getUserList());
    //     given(scheduleRepository.save(any(Schedule.class))).willReturn(schedule);
        
    //     //when
    //     Long scheduleId = scheduleService.createSchedule(request);
    // }
    
    // @Test(expected = NoSuchWorkspaceException.class)
    // public void 없는_워크스페이스에_스케줄_생성(){
    //     //given
    //     List<String> userNameList = new ArrayList<>();
        
    //     CreateScheduleRequest request = new CreateScheduleRequest("testWorkspace2", "testSchedule1", userNameList);
        
    //     given(workspaceRepository.findByName("testWorkspace2")).willReturn(Optional.ofNullable(null));
        
    //     //when
    //     Long scheduleId = scheduleService.createSchedule(request);
    // }
    
    // @Test
    // public void 스케줄_수정(){
    //     //given
    //     List<User> userList = getUserList();
        
    //     List<String> userAccountIdList = new ArrayList<>();
    //     userAccountIdList.add("test1");
    //     userAccountIdList.add("test2");
        
    //     List<User> updateUserList = new ArrayList<>();
    //     updateUserList.add(userList.get(0));
    //     updateUserList.add(userList.get(1));
        
    //     Workspace workspace = new Workspace("testWorkspace1",userList);
        
    //     Schedule schedule = new Schedule(workspace, "testSchedule2", userList);
    //     Long scheduleId = 1L;
        
    //     UpdateScheduleRequest request = new UpdateScheduleRequest("testSchedule2", userAccountIdList);
        
        
    //     given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));
    //     given(userRepository.findUsersByAccounIdList(any(List.class))).willReturn(updateUserList);
    //     //when
    //     Schedule result = scheduleService.updateSchedule(scheduleId,request);
        
    //     //then
    //     assertThat(result.getName()).isEqualTo("testSchedule2");
    //     assertThat(result.getWorkspace().getName()).isEqualTo("testWorkspace1");
    //     assertThat(result.getUserSchedules().size()).isEqualTo(2);
    //     assertThat(result.getUserSchedules().get(0).getUser().getAccountId()).isEqualTo("test1");
    //     assertThat(result.getUserSchedules().get(1).getUser().getAccountId()).isEqualTo("test2");
    // }
    
    // @Test(expected = NoSuchScheduleException.class)
    // public void 없는_스케줄_수정(){
    //     //given
    //     List<String> userAccountIdList = new ArrayList<>();
    //     UpdateScheduleRequest request = new UpdateScheduleRequest("testSchedule2", userAccountIdList);
        
    //     Long undefinedId = 1L;
        
    //      given(scheduleRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));
        
    //     //when
    //     Schedule result = scheduleService.updateSchedule(undefinedId,request);
    // }
    
    // @Test
    // public void 스케줄_삭제(){
    //     //given
    //     Workspace workspace = new Workspace("testWorkspace1",getUserList());
    //     Schedule schedule = new Schedule(workspace, "testSchedule1", getUserList());
    //     Long scheduleId = 1L;
        
    //     given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));
        
    //     //when
    //     scheduleService.removeSchedule(scheduleId);
    // }
    
    // @Test(expected = NoSuchScheduleException.class)
    // public void 없는_스케줄_삭제() throws Exception{
    //     //given
    //     Workspace workspace = new Workspace("testWorkspace1",getUserList());
    //     Schedule schedule = new Schedule(workspace, "testSchedule1", getUserList());
    //     Long undefinedId = 1L;
        
    //     given(scheduleRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));
        
    //     //when
    //     scheduleService.removeSchedule(undefinedId);
    // }
    
    // @Test
    // public void 스케줄_유저_추가(){
    //     //given
    //     Workspace workspace = new Workspace("testWorkspace1",getUserList());
    //     Schedule schedule = new Schedule(workspace, "testSchedule1", getUserList());
    //     Long scheduleId = 1L;
        
    //     User user = new User("test4", "test1234!", "kim");
    //     String accountId = "test4";
        
    //     given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));
    //     given(userRepository.findByAccountId(accountId)).willReturn(Optional.of(user));        
        
    //     //when
    //     Schedule result = scheduleService.addUser(scheduleId, accountId);
        
    //     //then
    //     assertThat(result.getName()).isEqualTo("testSchedule1");
    //     assertThat(result.getWorkspace().getName()).isEqualTo("testWorkspace1");
    //     assertThat(result.getUserSchedules().size()).isEqualTo(4);
    //     assertThat(result.getUserSchedules().get(0).getUser().getAccountId()).isEqualTo("test1");
    //     assertThat(result.getUserSchedules().get(1).getUser().getAccountId()).isEqualTo("test2");
    //     assertThat(result.getUserSchedules().get(2).getUser().getAccountId()).isEqualTo("test3");
    //     assertThat(result.getUserSchedules().get(3).getUser().getAccountId()).isEqualTo("test4");
    // }
    
    // @Test(expected = NoSuchScheduleException.class)
    // public void 없는_스케줄_유저_추가(){
    //     //given
    //     Long undefinedId = 1L;
        
    //     String accountId = "test4";
        
    //     given(scheduleRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));      
        
    //     //when
    //     Schedule result = scheduleService.addUser(undefinedId, accountId);
    // }
    
    // @Test(expected = NoSuchUserException.class)
    // public void 스케줄_없는_유저_추가(){
    //     //given
    //     Workspace workspace = new Workspace("testWorkspace1",getUserList());
    //     Schedule schedule = new Schedule(workspace, "testSchedule1", getUserList());
    //     Long scheduleId = 1L;
        
    //     String undefinedAccountId = "test4";
        
    //     given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));      
    //     given(userRepository.findByAccountId(undefinedAccountId)).willReturn(Optional.ofNullable(null));  
        
    //     //when
    //     Schedule result = scheduleService.addUser(scheduleId, undefinedAccountId);
    // }
    
    // @Test(expected = IllegalStateException.class)
    // public void 스케줄_중복_유저_추가(){
    //     //given
    //     List<User> userList = getUserList();
        
    //     Workspace workspace = new Workspace("testWorkspace1", userList);
    //     Schedule schedule = new Schedule(workspace, "testSchedule1", userList);
    //     Long scheduleId = 1L;
        
    //     User user = userList.get(2);
    //     String accountId = "test3";
        
    //     given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));
    //     given(userRepository.findByAccountId(accountId)).willReturn(Optional.of(user)); 
        
    //     //when
    //     Schedule result = scheduleService.addUser(scheduleId, accountId);
    // }
    
    // @Test
    // public void 스케줄_유저_삭제(){
    //     //given
    //     List<User> userList = getUserList();
        
    //     Workspace workspace = new Workspace("testWorkspace1", userList);
    //     Schedule schedule = new Schedule(workspace, "testSchedule1", userList);
    //     Long scheduleId = 1L;
        
    //     User user = userList.get(2);
    //     Long userId = 2L;
        
    //     given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));  
    //     given(userRepository.findById(userId)).willReturn(Optional.of(user));        
        
    //     //when
    //     scheduleService.removeUser(scheduleId, userId);
        
    //     //then
    //     assertThat(schedule.getName()).isEqualTo("testSchedule1");
    //     assertThat(schedule.getWorkspace().getName()).isEqualTo("testWorkspace1");
    //     assertThat(schedule.getUserSchedules().size()).isEqualTo(2);
    //     assertThat(schedule.getUserSchedules().get(0).getUser().getAccountId()).isEqualTo("test1");
    //     assertThat(schedule.getUserSchedules().get(1).getUser().getAccountId()).isEqualTo("test2");
    // }
    
    // @Test(expected = NoSuchScheduleException.class)
    // public void 없는_스케줄_유저_삭제(){
    //     //given
    //     Long undefinedId = 1L;
        
    //     Long userId = 2L;
        
    //     given(scheduleRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));       
        
    //     //when
    //     scheduleService.removeUser(undefinedId, userId);
    // }
    
    // @Test(expected = NoSuchUserException.class)
    // public void 스케줄_없는_유저_삭제(){
    //     //given
    //     Workspace workspace = new Workspace("testWorkspace1", getUserList());
    //     Schedule schedule = new Schedule(workspace, "testSchedule1", getUserList());
    //     Long scheduleId = 1L;
        
    //     Long undefinedUserId = 2L;
        
    //     given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));     
    //     given(userRepository.findById(undefinedUserId)).willReturn(Optional.ofNullable(null));  
        
    //     //when
    //     scheduleService.removeUser(scheduleId, undefinedUserId);
    // }
    
    // @Test(expected = IllegalStateException.class)
    // public void 스케줄_미참여_유저_삭제(){
    //     //given
    //     List<User> userList = getUserList();
        
    //     Workspace workspace = new Workspace("testWorkspace1", userList);
    //     Schedule schedule = new Schedule(workspace, "testSchedule1", userList);
    //     Long scheduleId = 1L;
        
    //     User user = new User("test4", "test1234!", "kim");
    //     Long userId = 4L;
        
    //     given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));  
    //     given(userRepository.findById(userId)).willReturn(Optional.of(user)); 
    //     //when
    //     scheduleService.removeUser(scheduleId, userId);
    // }
}