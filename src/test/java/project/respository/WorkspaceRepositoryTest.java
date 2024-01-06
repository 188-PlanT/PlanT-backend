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
// public class WorkspaceRepositoryTest{
    
    
//     @Autowired
//     private WorkspaceRepository workspaceRepository;
    
//     @Autowired
//     private UserRepository userRepository;
    
//     // @Before
//     // public void setUp(){
//     //     List<User> userList = new ArrayList<>();
//     //     userList.add(new User("test1", "test1234!", "kim"));
//     //     userList.add(new User("test2", "test1234!", "park"));
//     //     userList.add(new User("test3", "test1234!", "lee"));
        
//     //     for (User user: userList){
//     //         userRepository.save(user);
//     //     }
//     // }
    
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
    
//     @Test //워크스페이스 생성
//     public void test_save(){
//         //given
//         List<User> userList = getUsers();
//         Workspace workspace = new Workspace("testWorkspace1", userList);
        
//         // //when
//         Workspace saveWorkspace = workspaceRepository.save(workspace);
        
//         //then
//         assertThat(saveWorkspace.getName()).isEqualTo("testWorkspace1");
//         assertThat(saveWorkspace.getUserWorkspaces().size()).isEqualTo(3);
//         assertThat(saveWorkspace.getUserWorkspaces().get(0).getUser().getAccountId()).isEqualTo("test1");
//         assertThat(saveWorkspace.getUserWorkspaces().get(1).getUser().getAccountId()).isEqualTo("test2");
//         assertThat(saveWorkspace.getUserWorkspaces().get(2).getUser().getAccountId()).isEqualTo("test3");
//     }
    
//     @Test //워크스페이스 단일 조회
//     public void test_findById(){
//         //given
//         List<User> userList = getUsers();
//         Workspace workspace = new Workspace("testWorkspace1", userList);
//         Long workspaceId = workspaceRepository.save(workspace).getId();
        
//         //when
//         Optional<Workspace> findWorkspaceOptional = workspaceRepository.findById(workspaceId);
        
//         //then
//         assertThat(findWorkspaceOptional.isPresent()).isEqualTo(true);
        
//         Workspace findWorkspace = findWorkspaceOptional.get();
//         assertThat(findWorkspace.getName()).isEqualTo("testWorkspace1");
//         assertThat(findWorkspace.getUserWorkspaces().size()).isEqualTo(3);
//         assertThat(findWorkspace.getUserWorkspaces().get(0).getUser().getAccountId()).isEqualTo("test1");
//         assertThat(findWorkspace.getUserWorkspaces().get(1).getUser().getAccountId()).isEqualTo("test2");
//         assertThat(findWorkspace.getUserWorkspaces().get(2).getUser().getAccountId()).isEqualTo("test3");
//     }
    
//     @Test //워크스페이스 단일 조회 오류
//     public void test_findById_byUndefinedId(){
//         //given
//         List<User> userList = getUsers();
//         Workspace workspace = new Workspace("testWorkspace1", userList);
//         workspaceRepository.save(workspace);
        
//         Long undefinedId = 0L;
//         //when
//         Optional<Workspace> findWorkspaceOptional = workspaceRepository.findById(undefinedId);
//         //then
//         assertThat(findWorkspaceOptional.isPresent()).isEqualTo(false);
//     }
    
//     @Test //이름으로 존재 확인
//     public void test_existsByName(){
//         //given
//         List<User> userList = getUsers();
//         Workspace workspace = new Workspace("testWorkspace1", userList);
//         String workspaceName = workspaceRepository.save(workspace).getName();
//         //when
//         assertThat(workspaceRepository.existsByName(workspaceName))
//         //then
//         .isEqualTo(true);
//     }
    
//     @Test //유저 존재 확인 오류
//     public void test_existsByName_byUndefinedName(){
//         //given
//         List<User> userList = getUsers();
//         Workspace workspace = new Workspace("testWorkspace1", userList);
//         workspaceRepository.save(workspace);
        
//         String undefinedName = "undefined";
//         //when
//         assertThat(workspaceRepository.existsByName(undefinedName))
//         //then
//         .isEqualTo(false);
//     }
    
//     @Test //이름으로 조회
//     public void test_findByName(){
//         //given
//         List<User> userList = getUsers();
//         Workspace workspace = new Workspace("testWorkspace1", userList);
//         String workspaceName = workspaceRepository.save(workspace).getName();
        
//         //when
//         Optional<Workspace> findWorkspaceOptional = workspaceRepository.findByName(workspaceName);
        
//         //then
//         assertThat(findWorkspaceOptional.isPresent()).isEqualTo(true);
        
//         Workspace findWorkspace = findWorkspaceOptional.get();
//         assertThat(findWorkspace.getName()).isEqualTo("testWorkspace1");
//         assertThat(findWorkspace.getUserWorkspaces().size()).isEqualTo(3);
//         assertThat(findWorkspace.getUserWorkspaces().get(0).getUser().getAccountId()).isEqualTo("test1");
//         assertThat(findWorkspace.getUserWorkspaces().get(1).getUser().getAccountId()).isEqualTo("test2");
//         assertThat(findWorkspace.getUserWorkspaces().get(2).getUser().getAccountId()).isEqualTo("test3");
//     }
    
//     @Test //이름으로 조회 오류
//     public void test_findByName_byUndefinedName(){
//         //given
//         List<User> userList = getUsers();
//         Workspace workspace = new Workspace("testWorkspace1", userList);
//         workspaceRepository.save(workspace);
//         String undefinedName = "undefined";
        
//         //when
//         Optional<Workspace> findWorkspaceOptional = workspaceRepository.findByName(undefinedName);
        
//         //then
//         assertThat(findWorkspaceOptional.isPresent()).isEqualTo(false);
//     }
    
//     @Test //전체 조회
//     public void test_findAll(){
//         //given    
//         List<User> userList = getUsers();
//         workspaceRepository.save(new Workspace("testWorkspace1", userList));
//         workspaceRepository.save(new Workspace("testWorkspace2", userList));
//         workspaceRepository.save(new Workspace("testWorkspace3", userList));
        
//         Pageable pageable = PageRequest.of(0,20);
        
//         //when
//         List<Workspace> workspaceList = workspaceRepository.findAll(pageable).getContent();
//         //then
//         assertThat(workspaceList.size()).isEqualTo(3);
//         assertThat(workspaceList.get(0).getName()).isEqualTo("testWorkspace1");
//         assertThat(workspaceList.get(1).getName()).isEqualTo("testWorkspace2");
//         assertThat(workspaceList.get(2).getName()).isEqualTo("testWorkspace3");
//     }
// }