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
// public class ScheduleRepositoryTest{
    
//     @Autowired
//     private ScheduleRepository scheduleRepository;
    
//     @Autowired
//     private WorkspaceRepository workspaceRepository;
    
//     @Autowired
//     private UserRepository userRepository;
    
    
//     private List<User> getUsers(){
//         List<User> userList = new ArrayList<>();
//         userList.add(new User("test1", "test1234!", "kim"));
//         userList.add(new User("test2", "test1234!", "park"));
//         userList.add(new User("test3", "test1234!", "lee"));
        
//         for (User user: userList){
//             userRepository.save(user);
//         }
        
//         return userList;
//     }
    
//     private Workspace getWorkspace(List<User> userList){
//         Workspace workspace = new Workspace("testWorkspace1", userList);
//         workspaceRepository.save(workspace);
//         return workspace;
//     }
    
//     @Test //스케줄 생성
//     public void test_save(){
//         //given
//         List<User> userList = getUsers();
//         Workspace workspace = getWorkspace(userList);
        
//         Schedule schedule = new Schedule(workspace, "testSchedule1", userList);
        
//         // //when
//         Schedule saveSchedule = scheduleRepository.save(schedule);
        
//         //then
//         assertThat(saveSchedule.getName()).isEqualTo("testSchedule1");
//         assertThat(saveSchedule.getWorkspace().getName()).isEqualTo("testWorkspace1");
//         assertThat(saveSchedule.getUserSchedules().size()).isEqualTo(3);
//         assertThat(saveSchedule.getUserSchedules().get(0).getUser().getAccountId()).isEqualTo("test1");
//         assertThat(saveSchedule.getUserSchedules().get(1).getUser().getAccountId()).isEqualTo("test2");
//         assertThat(saveSchedule.getUserSchedules().get(2).getUser().getAccountId()).isEqualTo("test3");
//     }
    
//     @Test //스케줄 단일 조회
//     public void test_findById(){
//         //given
//         List<User> userList = getUsers();
//         Workspace workspace = getWorkspace(userList);
//         Schedule schedule = new Schedule(workspace, "testSchedule1", userList);
        
//         Long scheduleId = scheduleRepository.save(schedule).getId();
        
//         //when
//         Optional<Schedule> findScheduleOptional = scheduleRepository.findById(scheduleId);
        
//         //then
//         assertThat(findScheduleOptional.isPresent()).isEqualTo(true);
        
//         Schedule findSchedule = findScheduleOptional.get();
//         assertThat(findSchedule.getName()).isEqualTo("testSchedule1");
//         assertThat(findSchedule.getWorkspace().getName()).isEqualTo("testWorkspace1");
//         assertThat(findSchedule.getUserSchedules().size()).isEqualTo(3);
//         assertThat(findSchedule.getUserSchedules().get(0).getUser().getAccountId()).isEqualTo("test1");
//         assertThat(findSchedule.getUserSchedules().get(1).getUser().getAccountId()).isEqualTo("test2");
//         assertThat(findSchedule.getUserSchedules().get(2).getUser().getAccountId()).isEqualTo("test3");
//     }
    
//     @Test //스케줄 단일 조회 오류
//     public void test_findById_byUndefinedId(){
//         //given
//         List<User> userList = getUsers();
//         Workspace workspace = getWorkspace(userList);
//         Schedule schedule = new Schedule(workspace, "testSchedule1", userList);
        
//         Long undefinedId = 0L;
//         //when
//         Optional<Workspace> findWorkspaceOptional = workspaceRepository.findById(undefinedId);
//         //then
//         assertThat(findWorkspaceOptional.isPresent()).isEqualTo(false);
//     }
    
//     @Test //전체 조회
//     public void test_findAll(){
//         //given    
//         List<User> userList = getUsers();
//         Workspace workspace = getWorkspace(userList);
//         scheduleRepository.save(new Schedule(workspace, "testSchedule1", userList));
//         scheduleRepository.save(new Schedule(workspace, "testSchedule2", userList));
//         scheduleRepository.save(new Schedule(workspace, "testSchedule3", userList));
        
//         Pageable pageable = PageRequest.of(0,20);
        
//         //when
//         List<Schedule> scheduleList = scheduleRepository.findAll(pageable).getContent();
//         //then
//         assertThat(scheduleList.size()).isEqualTo(3);
//         assertThat(scheduleList.get(0).getName()).isEqualTo("testSchedule1");
//         assertThat(scheduleList.get(1).getName()).isEqualTo("testSchedule2");
//         assertThat(scheduleList.get(2).getName()).isEqualTo("testSchedule3");
//     }
// }