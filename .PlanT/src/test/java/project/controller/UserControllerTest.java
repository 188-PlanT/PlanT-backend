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
// import project.dto.user.*;
// import project.service.UserService;
// import project.repository.UserRepository;
// import java.util.List;  
// import java.util.ArrayList;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.PageRequest;


// @RunWith(SpringRunner.class)
// @WebMvcTest(UserController.class)
// // @AutoConfigureMockMvc
// public class UserControllerTest{
//     @Autowired
//     MockMvc mvc;
    
//     @MockBean
//     private UserService userService;
    
//     @MockBean
//     private UserRepository userRepository;
    
//     @Test
//     public void 유저_리스트_출력() throws Exception {
//         //given
//         List<User> userList = new ArrayList<>();
//         userList.add(new User("test1", "test1234", "kim"));
//         userList.add(new User("test2", "test1234", "park"));
//         userList.add(new User("test3", "test1234", "kim"));
        
//         Pageable pageable = PageRequest.of(0,20);
        
//         Page<User> pageResult = new PageImpl<>(userList, pageable, 3);
        
//         given(userService.findAllBySearch(any(Pageable.class),any(),any())).willReturn(pageResult);
        
//         //when
//         mvc.perform(get("/users"))
//         //then
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.data[0].accountId").value("test1"))
//             .andExpect(jsonPath("$.data[1].accountId").value("test2"))
//             .andExpect(jsonPath("$.data[2].accountId").value("test3"));
//     }
    
//     @Test
//     public void 유저_생성() throws Exception {
//         //given
//         String request = "{ \"accountId\" : \"test1\" , \"password\" : \"Test1234\" , \"name\" : \"kim\" }";
        
//         given(userService.register(any(User.class))).willReturn(1L);
        
//         //when
//         mvc.perform(post("/users")            
//             .content(request)
//             .contentType(MediaType.APPLICATION_JSON))
//         //then
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$").value("1"));
//     }
    
//     @Test
//     public void 단일_유저_출력() throws Exception {
//         //given
//         User user = new User("test1", "test1234", "kim");
        
//         given(userService.findOne(any(Long.class))).willReturn(user);
        
//         //when
//         mvc.perform(get("/users/1"))
//         //then
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.accountId").value("test1"))
//             .andExpect(jsonPath("$.name").value("kim"));
//     }
    
//     @Test
//     public void 유저_수정() throws Exception {
//         //given
//         User user = new User("test1", "Test1234", "kim");
        
//         String request = "{ \"password\" : \"Test1234\" , \"name\" : \"kim\" }";
        
//         given(userService.updateUser(any(Long.class),any(String.class),any(String.class))).willReturn(user);
//         //when
//         mvc.perform(put("/users/1")
//             .content(request)
//             .contentType(MediaType.APPLICATION_JSON))
//         //then
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.accountId").value("test1"))
//             .andExpect(jsonPath("$.name").value("kim"));
//     }
    
//     @Test
//     public void 유저_삭제() throws Exception {
//         //given
        
//         // given(userService.findOne(any(Long.class))).willReturn(user);
        
//         //when
//         mvc.perform(delete("/users/1"))
//         //then
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.message").value("successfully delete User"));
//     }
// }