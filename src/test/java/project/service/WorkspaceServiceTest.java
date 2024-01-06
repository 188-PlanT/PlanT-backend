// package project.service;

// import static org.mockito.BDDMockito.*;
// import static org.mockito.ArgumentMatchers.any;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.junit.runner.RunWith;
// import org.mockito.junit.MockitoJUnitRunner;
// import org.junit.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import static org.assertj.core.api.Assertions.assertThat;

// import project.repository.UserRepository;
// import project.repository.WorkspaceRepository;
// import project.domain.*;
// import project.dto.workspace.*;
// import project.exception.user.*;
// import project.exception.workspace.*;
// import java.util.Optional;
// import java.util.List;
// import java.util.ArrayList;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.PageRequest;

// @RunWith(MockitoJUnitRunner.class)
// public class WorkspaceServiceTest{
    
//     @Mock
//     private UserRepository userRepository;
    
//     @Mock
//     private WorkspaceRepository workspaceRepository;
    
//     @InjectMocks
//     private WorkspaceServiceImpl workspaceService;
    
//     // 유저 생성 메소드
//     private List<User> getUserList(){
//         List<User> userList = new ArrayList<>();
        
//         userList.add(new User("test1", "test1234", "kim"));
//         userList.add(new User("test2", "test1234", "park"));
//         userList.add(new User("test3", "test1234", "lee"));
                              
//         return userList;
//     }
    
//     @Test
//     public void 워크스페이스_생성(){
//         //given
//         List<String> userNameList = new ArrayList<>();
//         userNameList.add("test1");
//         userNameList.add("test2");
//         userNameList.add("test3");
        
//         CreateWorkspaceRequest request = new CreateWorkspaceRequest("testWorkspace",userNameList);
        
//         Workspace workspace = new Workspace("testWorkspace",getUserList());
        
//         given(workspaceRepository.existsByName("testWorkspace")).willReturn(false);
//         given(userRepository.findUsersByAccounIdList(any(List.class))).willReturn(getUserList());
//         given(workspaceRepository.save(any(Workspace.class))).willReturn(workspace);
        
//         //when
//         Long workspaceId = workspaceService.makeWorkspace(request);
//     }
    
//     @Test(expected = IllegalStateException.class)
//     public void 워크스페이스_중복_생성() throws Exception{
//         //given
//         List<String> userNameList = new ArrayList<>();
        
//         CreateWorkspaceRequest request = new CreateWorkspaceRequest("testWorkspace",userNameList);
        
//         given(workspaceRepository.existsByName("testWorkspace")).willReturn(true);
        
//         //when
//         Long workspaceId = workspaceService.makeWorkspace(request);
//     }
    
//     @Test
//     public void 워크스페이스_삭제(){
//         //given
//         Workspace workspace = new Workspace("testWorkspace1",getUserList());
//         Long workspaceId = 1L;
        
//         given(workspaceRepository.findById(workspaceId)).willReturn(Optional.of(workspace));
        
//         //when
//         workspaceService.removeWorkspace(workspaceId);
//     }
    
//     @Test(expected = NoSuchWorkspaceException.class)
//     public void 없는_워크스페이스_삭제() throws Exception{
//         //given
//         Workspace workspace = new Workspace("testWorkspace1",getUserList());
//         Long undefinedId = 1L;
        
//         given(workspaceRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));
        
//         //when
//         workspaceService.removeWorkspace(undefinedId);
//     }
    
//     @Test
//     public void 워크스페이스_단건_조회(){
//         //given
//         Workspace workspace = new Workspace("testWorkspace1",getUserList());
//         Long workspaceId = 1L;
        
//         given(workspaceRepository.findById(workspaceId)).willReturn(Optional.of(workspace));
        
//         //when
//         Workspace result = workspaceService.findOne(workspaceId);
        
//         //then
//         assertThat(result.getName()).isEqualTo("testWorkspace1");
//         assertThat(result.getUserWorkspaces().get(0).getUser().getAccountId()).isEqualTo("test1");
//         assertThat(result.getUserWorkspaces().get(1).getUser().getAccountId()).isEqualTo("test2");
//         assertThat(result.getUserWorkspaces().get(2).getUser().getAccountId()).isEqualTo("test3");
//     }
    
//     @Test(expected = NoSuchWorkspaceException.class)
//     public void 없는_워크스페이스_조회() throws Exception{
//         //given
//         Long undefinedId = 1L;
        
//         given(workspaceRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));
        
//         //when
//         Workspace result = workspaceService.findOne(undefinedId);
//     }
    
//     @Test
//     public void 워크스페이스_리스트_조회(){
//         //given
//         List<Workspace> workspaceList = new ArrayList<>();
//         workspaceList.add(new Workspace("testWorkspace1", getUserList()));
//         workspaceList.add(new Workspace("testWorkspace2", getUserList()));
//         workspaceList.add(new Workspace("testWorkspace3", getUserList()));
        
//         Pageable pageable = PageRequest.of(0,20);
        
//         Page<Workspace> response = new PageImpl<>(workspaceList, pageable, 3);
        
//         given(workspaceRepository.findAll(any(Pageable.class))).willReturn(response);
        
//         //when
//         List<Workspace> result = workspaceService.findAll(pageable).getContent();
        
//         //then
//         assertThat(result.size()).isEqualTo(3);
//         assertThat(result.get(0).getName()).isEqualTo("testWorkspace1");
//         assertThat(result.get(1).getName()).isEqualTo("testWorkspace2");
//         assertThat(result.get(2).getName()).isEqualTo("testWorkspace3");
//     }
    
    
//     @Test
//     public void 워크스페이스_수정(){
//         //given
//         List<String> userNameList = new ArrayList<>();
//         userNameList.add("test1");
//         userNameList.add("test2");

//         List<User> updateUserList = new ArrayList<>();
//         updateUserList.add(new User("test1", "test1234", "kim"));
//         updateUserList.add(new User("test2", "test1234", "park"));
        
//         CreateWorkspaceRequest request = new CreateWorkspaceRequest("testWorkspace2",userNameList);
        
//         Workspace workspace = new Workspace("testWorkspace1",getUserList());
//         Long workspaceId = 1L;
        
//         given(workspaceRepository.findById(workspaceId)).willReturn(Optional.of(workspace));
//         given(userRepository.findUsersByAccounIdList(any(List.class))).willReturn(updateUserList);
//         //when
//         Workspace result = workspaceService.updateWorkspace(workspaceId,request);
        
//         //then
//         assertThat(result.getName()).isEqualTo("testWorkspace2");
//         assertThat(result.getUserWorkspaces().size()).isEqualTo(2);
//         assertThat(result.getUserWorkspaces().get(0).getUser().getAccountId()).isEqualTo("test1");
//         assertThat(result.getUserWorkspaces().get(1).getUser().getAccountId()).isEqualTo("test2");
//     }

//     @Test
//     public void 워크스페이스_이름동일_수정(){
//         //given
//         List<String> userNameList = new ArrayList<>();
//         userNameList.add("test1");
//         userNameList.add("test2");

//         List<User> updateUserList = new ArrayList<>();
//         updateUserList.add(new User("test1", "test1234", "kim"));
//         updateUserList.add(new User("test2", "test1234", "park"));
        
//         CreateWorkspaceRequest request = new CreateWorkspaceRequest("testWorkspace1",userNameList);
        
//         Workspace workspace = new Workspace("testWorkspace1",getUserList());
//         Long workspaceId = 1L;
        
//         given(workspaceRepository.findById(workspaceId)).willReturn(Optional.of(workspace));
//         given(userRepository.findUsersByAccounIdList(any(List.class))).willReturn(updateUserList);
//         //when
//         Workspace result = workspaceService.updateWorkspace(workspaceId,request);
        
//         //then
//         assertThat(result.getName()).isEqualTo("testWorkspace1");
//         assertThat(result.getUserWorkspaces().size()).isEqualTo(2);
//         assertThat(result.getUserWorkspaces().get(0).getUser().getAccountId()).isEqualTo("test1");
//         assertThat(result.getUserWorkspaces().get(1).getUser().getAccountId()).isEqualTo("test2");
//     }
    
//     @Test(expected = NoSuchWorkspaceException.class)
//     public void 없는_워크스페이스_수정(){
//         //given
//         List<String> userNameList = new ArrayList<>();
//         CreateWorkspaceRequest request = new CreateWorkspaceRequest("testWorkspace2",userNameList);
        
//         Long undefinedId = 1L;
        
//         given(workspaceRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));
        
//         //when
//         Workspace result = workspaceService.updateWorkspace(undefinedId,request);
//     }
    
//     @Test
//     public void 워크스페이스_유저_추가(){
//         //given
//         Workspace workspace = new Workspace("testWorkspace1",getUserList());
//         Long workspaceId = 1L;
        
//         User user = new User("test4", "test1234!", "kim");
//         String accountId = "test4";
        
//         given(workspaceRepository.findById(workspaceId)).willReturn(Optional.of(workspace));
//         given(userRepository.findByAccountId(accountId)).willReturn(Optional.of(user));        
        
//         //when
//         Workspace result = workspaceService.addUser(workspaceId, accountId);
        
//         //then
//         assertThat(result.getName()).isEqualTo("testWorkspace1");
//         assertThat(result.getUserWorkspaces().size()).isEqualTo(4);
//         assertThat(result.getUserWorkspaces().get(0).getUser().getAccountId()).isEqualTo("test1");
//         assertThat(result.getUserWorkspaces().get(1).getUser().getAccountId()).isEqualTo("test2");
//         assertThat(result.getUserWorkspaces().get(2).getUser().getAccountId()).isEqualTo("test3");
//         assertThat(result.getUserWorkspaces().get(3).getUser().getAccountId()).isEqualTo("test4");
//     }
    
//     @Test(expected = NoSuchWorkspaceException.class)
//     public void 없는_워크스페이스_유저_추가(){
//         //given
//         Long undefinedId = 1L;
        
//         String accountId = "test4";
        
//         given(workspaceRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));      
        
//         //when
//         Workspace result = workspaceService.addUser(undefinedId, accountId);
//     }
    
//     @Test(expected = NoSuchUserException.class)
//     public void 워크스페이스_없는_유저_추가(){
//         //given
//         Workspace workspace = new Workspace("testWorkspace1",getUserList());
//         Long workspaceId = 1L;
        
//         String undefinedAccountId = "test4";
        
//         given(workspaceRepository.findById(workspaceId)).willReturn(Optional.of(workspace));      
//         given(userRepository.findByAccountId(undefinedAccountId)).willReturn(Optional.ofNullable(null));  
        
//         //when
//         Workspace result = workspaceService.addUser(workspaceId, undefinedAccountId);
//     }
    
//     @Test(expected = IllegalStateException.class)
//     public void 워크스페이스_중복_유저_추가(){
//         //given
//         List<User> userList = getUserList();
        
//         Workspace workspace = new Workspace("testWorkspace1",userList);
//         Long workspaceId = 1L;
        
//         User user = userList.get(2);
//         String accountId = "test3";
        
//         given(workspaceRepository.findById(workspaceId)).willReturn(Optional.of(workspace));
//         given(userRepository.findByAccountId(accountId)).willReturn(Optional.of(user)); 
//         //when
//         Workspace result = workspaceService.addUser(workspaceId, accountId);
//     }
    
//     @Test
//     public void 워크스페이스_유저_삭제(){
//         //given
//         List<User> userList = getUserList();
        
//         Workspace workspace = new Workspace("testWorkspace1",userList);
//         Long workspaceId = 1L;
        
//         User user = userList.get(2);
//         Long userId = 2L;
        
//         given(workspaceRepository.findById(workspaceId)).willReturn(Optional.of(workspace));
//         given(userRepository.findById(userId)).willReturn(Optional.of(user));        
        
//         //when
//         workspaceService.removeUser(workspaceId, userId);
        
//         //then
//         assertThat(workspace.getName()).isEqualTo("testWorkspace1");
//         assertThat(workspace.getUserWorkspaces().size()).isEqualTo(2);
//         assertThat(workspace.getUserWorkspaces().get(0).getUser().getAccountId()).isEqualTo("test1");
//         assertThat(workspace.getUserWorkspaces().get(1).getUser().getAccountId()).isEqualTo("test2");
//     }
    
//     @Test(expected = NoSuchWorkspaceException.class)
//     public void 없는_워크스페이스_유저_삭제(){
//         //given
//         Long undefinedId = 1L;
        
//         Long userId = 2L;
        
//         given(workspaceRepository.findById(undefinedId)).willReturn(Optional.ofNullable(null));      
        
//         //when
//         workspaceService.removeUser(undefinedId, userId);
//     }
    
//     @Test(expected = NoSuchUserException.class)
//     public void 워크스페이스_없는_유저_삭제(){
//         //given
//         Workspace workspace = new Workspace("testWorkspace1",getUserList());
//         Long workspaceId = 1L;
        
//         Long undefinedUserId = 2L;
        
//         given(workspaceRepository.findById(workspaceId)).willReturn(Optional.of(workspace));      
//         given(userRepository.findById(undefinedUserId)).willReturn(Optional.ofNullable(null));  
        
//         //when
//         workspaceService.removeUser(workspaceId, undefinedUserId);
//     }
    
//     @Test(expected = IllegalStateException.class)
//     public void 워크스페이스_미참여_유저_삭제(){
//         //given
//         List<User> userList = getUserList();
        
//         Workspace workspace = new Workspace("testWorkspace1",userList);
//         Long workspaceId = 1L;
        
//         User user = new User("test4", "test1234!", "kim");
//         Long userId = 4L;
        
//         given(workspaceRepository.findById(workspaceId)).willReturn(Optional.of(workspace));
//         given(userRepository.findById(userId)).willReturn(Optional.of(user)); 
//         //when
//         workspaceService.removeUser(workspaceId, userId);
//     }
// }