// package project.api;

// import project.common.IntegrationTest;

// import org.springframework.http.MediaType;
// import org.springframework.http.HttpHeaders;

// import org.junit.jupiter.api.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// public class UserControllerTest extends IntegrationTest {
	
// 	@Test
//     public void 이메일_검증() throws Exception {
//         //given
//         String request = "{ \"email\" : \"test1@gmail.com\" }";

//         //when
//         mvc.perform(post("/v1/users/email")            
//             .content(request)
//             .contentType(MediaType.APPLICATION_JSON))
//         //then
//             .andExpect(status().isOk())
// 			.andExpect(jsonPath("$.available").value("false"));
//     }
// }