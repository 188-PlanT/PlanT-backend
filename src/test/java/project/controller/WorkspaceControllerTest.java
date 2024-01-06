// package project.controller;

// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import org.springframework.http.MediaType;

// import org.junit.runner.RunWith;
// import org.springframework.test.context.junit4.SpringRunner;
// import org.junit.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import static org.mockito.BDDMockito.given;
// import static org.mockito.ArgumentMatchers.any;

// import project.domain.*;
// import project.dto.workspace.*;
// import project.service.WorkspaceService;
// import project.repository.UserRepository;
// import java.util.List;  
// import java.util.ArrayList;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.PageRequest;


// @RunWith(SpringRunner.class)
// @WebMvcTest(WorkspaceController.class)
// @AutoConfigureMockMvc
// public class WorkspaceControllerTest{
//     @Autowired
//     MockMvc mvc;
    
//     @MockBean
//     private WorkspaceService workspaceService;
    
//     // 유저 생성 메소드
//     private List<User> getUserList(){
//         List<User> userList = new ArrayList<>();
        
//         userList.add(new User("test1", "test1234", "kim"));
//         userList.add(new User("test2", "test1234", "park"));
//         userList.add(new User("test3", "test1234", "lee"));
                              
//         return userList;
//     }
    
//     @Test
//     public void 워크스페이스_리스트_출력() throws Exception {
//         //given
//         List<Workspace> workspaceList = new ArrayList<>();
//         workspaceList.add(new Workspace("testWorkspace1", getUserList()));
//         workspaceList.add(new Workspace("testWorkspace2", getUserList()));
//         workspaceList.add(new Workspace("testWorkspace3", getUserList()));
        
//         Pageable pageable = PageRequest.of(0,20);
        
//         Page<Workspace> response = new PageImpl<>(workspaceList, pageable, 3);
        
//         given(workspaceService.findAll(any(Pageable.class))).willReturn(response);
        
//         //when
//         mvc.perform(get("/workspaces"))
//         //then
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.data[0].name").value("testWorkspace1"))
//             .andExpect(jsonPath("$.data[1].name").value("testWorkspace2"))
//             .andExpect(jsonPath("$.data[2].name").value("testWorkspace3"));
//     }
    
//     @Test //Validation check??
//     public void 워크스페이스_생성() throws Exception {
//         //given
//         String request = "{ \"name\" : \"testWorkspace1\" , \"users\" : [ \"test1\" , \"test2\" , \"test3\" ] }";
        
//         given(workspaceService.makeWorkspace(any(CreateWorkspaceRequest.class))).willReturn(1L);
        
//         //when
//         mvc.perform(post("/workspaces")            
//             .content(request)
//             .contentType(MediaType.APPLICATION_JSON))
//         //then
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$").value("1"));
//     }
    
//     @Test
//     public void 단일_워크스페이스_출력() throws Exception {
//         //given
//         Workspace workspace = new Workspace("testWorkspace1", getUserList());
        
//         given(workspaceService.findOne(any(Long.class))).willReturn(workspace);
        
//         //when
//         mvc.perform(get("/workspaces/1"))
//         //then
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.name").value("testWorkspace1"))
//             .andExpect(jsonPath("$.users[0]").value("test1"))
//             .andExpect(jsonPath("$.users[1]").value("test2"))
//             .andExpect(jsonPath("$.users[2]").value("test3"));
//     }
    
//     @Test
//     public void 워크스페이스_수정() throws Exception {
//         //given
//         String request = "{ \"name\" : \"testWorkspace2\" , \"users\" : [ \"test1\" , \"test2\" , \"test3\" ] }";
//         Workspace workspace = new Workspace("testWorkspace2", getUserList());
        
//         given(workspaceService.updateWorkspace(any(Long.class),any(CreateWorkspaceRequest.class))).willReturn(workspace);
//         //when
//         mvc.perform(put("/workspaces/1")
//             .content(request)
//             .contentType(MediaType.APPLICATION_JSON))
//         //then
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.name").value("testWorkspace2"))
//             .andExpect(jsonPath("$.users[0]").value("test1"))
//             .andExpect(jsonPath("$.users[1]").value("test2"))
//             .andExpect(jsonPath("$.users[2]").value("test3"));
//     }
    
//     @Test
//     public void 워크스페이스_삭제() throws Exception {
//         //when
//         mvc.perform(delete("/workspaces/1"))
//         //then
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.message").value("successfully delete workspace"));
//     }
    
//     @Test
//     public void 워크스페이스_유저_상세출력() throws Exception {
//         //given
//         Workspace workspace = new Workspace("testWorkspace1", getUserList());
        
//         given(workspaceService.findOne(any(Long.class))).willReturn(workspace);
        
//         //when
//         mvc.perform(get("/workspaces/1/users"))
//         //then
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.name").value("testWorkspace1"))
//             .andExpect(jsonPath("$.users[0].userAccountId").value("test1"))
//             .andExpect(jsonPath("$.users[1].userAccountId").value("test2"))
//             .andExpect(jsonPath("$.users[2].userAccountId").value("test3"))
//             .andExpect(jsonPath("$.users[0].userName").value("kim"))
//             .andExpect(jsonPath("$.users[1].userName").value("park"))
//             .andExpect(jsonPath("$.users[2].userName").value("lee"));
//     }
    
//     @Test
//     public void 워크스페이스_유저_추가() throws Exception {
//         //given
//         Workspace workspace = new Workspace("testWorkspace1", getUserList());
//         String request = "{ \"accountId\" : \"test4\" }";
        
//         given(workspaceService.addUser(any(Long.class), any(String.class))).willReturn(workspace);
        
//         //when
//         mvc.perform(post("/workspaces/1/users")            
//             .content(request)
//             .contentType(MediaType.APPLICATION_JSON))
//         //then
//             .andExpect(status().isOk());
//     }
    
//     @Test
//     public void 워크스페이스_유저_삭제() throws Exception {
//         //when
//         mvc.perform(delete("/workspaces/1/users/1"))
//         //then
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.message").value("successfully delete user"));
//     }
// }