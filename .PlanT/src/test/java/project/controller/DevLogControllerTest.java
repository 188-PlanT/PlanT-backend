// package project.controller;

// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import org.junit.runner.RunWith;
// import org.springframework.test.context.junit4.SpringRunner;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import project.domain.*;
// import project.service.DevLogService;
// import project.repository.DevLogRepository;
// import java.util.List;  
// import java.util.ArrayList;

// @RunWith(SpringRunner.class)
// @WebMvcTest(DevLogController.class)
// @AutoConfigureMockMvc
// class DevLogControllerTest{
//     @Autowired
//     MockMvc mvc;
    
//     @MockBean
//     private DevLogService devLogService;
    
//     @MockBean
//     private DevLogRepository devLogRepository;
    
//     @Test
//     void getAllDevLogTest() throws Exception {
//         //given
//         User user = new User("test1","pw1234","test1Kim");
        
//         List<User> userList = new ArrayList<> ();
//         userList.add(user);
        
//         Workspace workspace = new Workspace("testWorkspace",userList);
//         Schedule schedule = new Schedule(workspace,"testSchedule",userList);
//         DevLog devLog = new DevLog(schedule, user, "testDevLog");
        
//         devLogRepository.save(devLog);
//         //when
        
//         mvc.perform(post("/devLogs"))
//             .content()
        
//         //then
//         mvc.perform(get("/devLogs"))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.count").value("1"))
//             .andDo(print());
//     }
// }