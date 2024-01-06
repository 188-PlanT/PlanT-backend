// package project.repository;

// import org.junit.Test;
// import org.junit.Before;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.junit.runner.RunWith;
// import org.springframework.test.context.junit4.SpringRunner;
// import org.springframework.test.context.web.WebAppConfiguration;
// import org.springframework.context.annotation.Import;
// import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import static org.assertj.core.api.Assertions.assertThat;

// import project.AppConfig;
// import project.domain.*;
// import java.util.Optional;
// import java.util.List;
// import java.util.ArrayList;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.PageRequest;


// @RunWith(SpringRunner.class)
// @DataJpaTest
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// @Import(AppConfig.class)
// public class DevLogRepositoryTest{
    
//     @Autowired
//     private DevLogRepository devLogRepository;
    
//     @Autowired
//     private ScheduleRepository scheduleRepository;
    
//     @Autowired
//     private WorkspaceRepository workspaceRepository;
    
//     @Autowired
//     private UserRepository userRepository;
    
//     private Workspace getWorkspace(User user){
//         List<User> userList = new ArrayList<>();
//         userList.add(user);
        
//         Workspace workspace = new Workspace("testWorkspace1", userList);
//         workspaceRepository.save(workspace);
        
//         return workspace;
//     }
    
//     private Schedule getSchedule(User user, Workspace workspace){
//         List<User> userList = new ArrayList<>();
//         userList.add(user);
        
//         Schedule schedule = new Schedule(workspace, "testSchedule1", userList);
//         scheduleRepository.save(schedule);
        
//         return schedule;
//     }
    
//     @Test //개발일지 생성
//     public void test_save(){
//         //given
//         User user = new User("test1", "test1234!", "kim");
//         userRepository.save(user);
        
//         Workspace workspace = getWorkspace(user);
//         Schedule schedule = getSchedule(user, workspace);
        
//         DevLog devLog = new DevLog(schedule, user, "testDevLog");
        
//         //when
//         DevLog saveDevLog = devLogRepository.save(devLog);
        
//         //then
//         assertThat(saveDevLog.getSchedule().getName()).isEqualTo("testSchedule1");
//         assertThat(saveDevLog.getUser().getAccountId()).isEqualTo("test1");
//         assertThat(saveDevLog.getContent()).isEqualTo("testDevLog");
//     }
    
//     @Test //개발일지 단일 조회
//     public void test_findById(){
//         //given
//         User user = new User("test1", "test1234!", "kim");
//         userRepository.save(user);
//         Workspace workspace = getWorkspace(user);
//         Schedule schedule = getSchedule(user, workspace);
        
//         DevLog devLog = new DevLog(schedule, user, "testDevLog");
        
//         Long devLogId = devLogRepository.save(devLog).getId();
        
//         //when
//         Optional<DevLog> findDevLogOptional = devLogRepository.findById(devLogId);
        
//         //then
//         assertThat(findDevLogOptional.isPresent()).isEqualTo(true);
        
//         DevLog findDevLog = findDevLogOptional.get();
//         assertThat(findDevLog.getSchedule().getName()).isEqualTo("testSchedule1");
//         assertThat(findDevLog.getUser().getAccountId()).isEqualTo("test1");
//         assertThat(findDevLog.getContent()).isEqualTo("testDevLog");
//     }
    
//     @Test //개발일지 단일 조회 오류
//     public void test_findById_byUndefinedId(){
//         //given
//         Long undefinedId = 0L;
        
//         //when
//         Optional<DevLog> findDevLogOptional = devLogRepository.findById(undefinedId);
        
//         //then
//         assertThat(findDevLogOptional.isPresent()).isEqualTo(false);
//     }
    
//     @Test //개발일지 존재 확인
//     public void test_existsByScheduleAndUser(){
//         //given
//         User user = new User("test1", "test1234!", "kim");
//         userRepository.save(user);
//         Workspace workspace = getWorkspace(user);
//         Schedule schedule = getSchedule(user, workspace);
        
//         devLogRepository.save(new DevLog(schedule, user, "testDevLog"));
        
//         User unvalidUser = new User("test2", "test1234!", "kim");
//         userRepository.save(unvalidUser);
//         Schedule unvalidSchedule = getSchedule(unvalidUser, workspace);

//         //then
//         assertThat(devLogRepository.existsByScheduleAndUser(schedule, user)).isEqualTo(true);
//         assertThat(devLogRepository.existsByScheduleAndUser(unvalidSchedule, user)).isEqualTo(false);
//         assertThat(devLogRepository.existsByScheduleAndUser(schedule, unvalidUser)).isEqualTo(false);
//     }
    
    
//     @Test //전체 조회&검색
//     public void test_searchDevLogs(){
//         //given    
//         User user1 = new User("test1", "test1234!", "kim");
//         userRepository.save(user1);
//         User user2 = new User("test2", "test1234!", "park"); 
//         userRepository.save(user2);
//         Workspace workspace = getWorkspace(user1);
        
//         Schedule schedule1 = getSchedule(user1, workspace);
//         Schedule schedule2 = getSchedule(user2, workspace);
        
//         // 스케줄에 등록 안된 유저도 devLog 작성 가능함..
//         devLogRepository.save(new DevLog(schedule1, user1, "testDevLog1"));
//         devLogRepository.save(new DevLog(schedule1, user2, "testDevLog2"));
//         devLogRepository.save(new DevLog(schedule2, user1, "testDevLog3"));
//         devLogRepository.save(new DevLog(schedule2, user2, "testDevLog4"));
        
//         Pageable pageable = PageRequest.of(0,20);
        
//         //when
//         List<DevLog> devLogList = devLogRepository.searchDevLogs(pageable, null, null).getContent();
        
//         List<DevLog> devLogListBySchedule = devLogRepository.searchDevLogs(pageable, schedule1, null).getContent();

//         List<DevLog> devLogListByUser = devLogRepository.searchDevLogs(pageable, null, user1).getContent();
        
//         List<DevLog> devLogListByScheduleAndUser = devLogRepository.searchDevLogs(pageable, schedule1, user1).getContent();
        
//         //then
//         assertThat(devLogList.size()).isEqualTo(4);
//         assertThat(devLogList.get(0).getContent()).isEqualTo("testDevLog1");
//         assertThat(devLogList.get(1).getContent()).isEqualTo("testDevLog2");
//         assertThat(devLogList.get(2).getContent()).isEqualTo("testDevLog3");
//         assertThat(devLogList.get(3).getContent()).isEqualTo("testDevLog4");
        
//         assertThat(devLogListBySchedule.size()).isEqualTo(2);
//         assertThat(devLogListBySchedule.get(0).getContent()).isEqualTo("testDevLog1");
//         assertThat(devLogListBySchedule.get(1).getContent()).isEqualTo("testDevLog2");
        
//         assertThat(devLogListByUser.size()).isEqualTo(2);
//         assertThat(devLogListByUser.get(0).getContent()).isEqualTo("testDevLog1");
//         assertThat(devLogListByUser.get(1).getContent()).isEqualTo("testDevLog3");
        
//         assertThat(devLogListByScheduleAndUser.size()).isEqualTo(1);
//         assertThat(devLogListByScheduleAndUser.get(0).getContent()).isEqualTo("testDevLog1");
//     }
// }