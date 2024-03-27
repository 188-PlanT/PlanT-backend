package project.common;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

import org.junit.jupiter.api.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class ScheduleApiTest extends IntegrationTest {
	
	@Test
    public void 스케줄_생성() throws Exception {
        //given
        String request = "{ \"workspaceId\" : 1 ," 
						+ " \"name\" : \"testSchedule3\" ,"
						+ " \"users\" : [1, 2, 3] ,"  
  						+ " \"startDate\" : \"20240401:00:00\" ,"
						+ " \"endDate\" : \"20240401:00:00\" ," 
						+ " \"content\" : \"hihi\" ,"
						+ " \"state\" : \"TODO\" }";
        //when
        mvc.perform(post("/v1/schedules")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER)
    		.contentType(MediaType.APPLICATION_JSON)
			.content(request))
        //then
            .andExpect(status().isCreated())
			
			.andExpect(jsonPath("$.workspaceId").value("1"))
			.andExpect(jsonPath("$.workspaceName").value("testWorkspace1"))
			.andExpect(jsonPath("$.scheduleId").exists())
			.andExpect(jsonPath("$.name").value("testSchedule3"))
			
			.andExpect(jsonPath("$.users[0].userId").value("1"))
			.andExpect(jsonPath("$.users[0].nickName").value("test11"))
		
		
			.andExpect(jsonPath("$.users[1].userId").value("2"))
			.andExpect(jsonPath("$.users[1].nickName").value("test22"))
		
			.andExpect(jsonPath("$.users[2].userId").value("3"))
			.andExpect(jsonPath("$.users[2].nickName").value("test33"))
		
			.andExpect(jsonPath("$.users[3]").doesNotExist())
		
			.andExpect(jsonPath("$.startDate").value("20240401:00:00"))
			.andExpect(jsonPath("$.endDate").value("20240401:00:00"))
			.andExpect(jsonPath("$.content").value("hihi"))
			.andExpect(jsonPath("$.state").value("TODO"));
    }
	
	// @Test // !!!여기 통과하도록 로직 수정해야함!!!
	// public void 스케줄_생성_권한없음() throws Exception {
	// //given
	// String request = "{ \"workspaceId\" : 1 ," 
	// 					+ " \"name\" : \"testSchedule3\" ,"
	// 					+ " \"users\" : [1, 2, 3] ,"  
	// + " \"startDate\" : \"20240101:00:00\" ,"
	// 					+ " \"endDate\" : \"20240101:00:00\" ," 
	// 					+ " \"content\" : \"hihi\" ,"
	// 					+ " \"state\" : \"TODO\" }";
	// //when
	// mvc.perform(post("/v1/schedules")
	// 		.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PENDING)
	// .contentType(MediaType.APPLICATION_JSON)
	// 		.content(request))
	// //then
	// .andExpect(status().isForbidden());
	// }
	@Test
    public void 스케줄_상세_조회() throws Exception {
        //given
        //when
        mvc.perform(get("/v1/schedules/1")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER))
        //then
            .andExpect(status().isOk())
			
			.andExpect(jsonPath("$.workspaceId").value("1"))
			.andExpect(jsonPath("$.workspaceName").value("testWorkspace1"))
			.andExpect(jsonPath("$.scheduleId").value("1"))
			.andExpect(jsonPath("$.name").value("testSchedule1"))	
			
			.andExpect(jsonPath("$.users[0].userId").value("1"))
			.andExpect(jsonPath("$.users[0].nickName").value("test11"))
		
			.andExpect(jsonPath("$.users[1].userId").value("2"))
			.andExpect(jsonPath("$.users[1].nickName").value("test22"))
		
			.andExpect(jsonPath("$.users[2]").doesNotExist())
			
			.andExpect(jsonPath("$.startDate").value("20240401:00:00"))
			.andExpect(jsonPath("$.endDate").value("20240430:23:59"))
			.andExpect(jsonPath("$.content").value("hihi"))
			.andExpect(jsonPath("$.state").value("TODO"))
					   
			.andExpect(jsonPath("$.chatList[0].chatId").value("1"))
			.andExpect(jsonPath("$.chatList[0].userId").value("1"))
			.andExpect(jsonPath("$.chatList[0].nickName").value("test11"))
			.andExpect(jsonPath("$.chatList[0].content").value("test"))
			.andExpect(jsonPath("$.chatList[0].createDate").value("20240401:00:00:00"))
					   
			.andExpect(jsonPath("$.chatList[1].chatId").value("2"))
			.andExpect(jsonPath("$.chatList[1].userId").value("2"))
			.andExpect(jsonPath("$.chatList[1].nickName").value("test22"))
			.andExpect(jsonPath("$.chatList[1].content").value("test"))
			.andExpect(jsonPath("$.chatList[1].createDate").value("20240401:00:00:00"))

			.andExpect(jsonPath("$.chatList[2]").doesNotExist());
    }
	
	// @Test // !!!여기 통과하도록 로직 수정해야함!!!
	// public void 스케줄_조회_권한없음() throws Exception {
	// //given
	// //when
	// mvc.perform(get("/v1/schedules/1")
	// 		.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PENDING))
	// //then
	// .andExpect(status().isForbidden());
	// }
	
	@Test
    public void 스케줄_수정() throws Exception {
        //given
        String request = "{ \"name\" : \"testSchedule111\" ,"
						+ " \"users\" : [1, 2, 3] ,"  
  						+ " \"startDate\" : \"20240430:23:59\" ,"
						+ " \"endDate\" : \"20240501:00:00\" ," 
						+ " \"content\" : \"hihi\" ,"
						+ " \"state\" : \"DONE\" }";
        //when
        mvc.perform(put("/v1/schedules/1")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER)
    		.contentType(MediaType.APPLICATION_JSON)
			.content(request))
        //then
            .andExpect(status().isOk())
			
			.andExpect(jsonPath("$.workspaceId").value("1"))
			.andExpect(jsonPath("$.workspaceName").value("testWorkspace1"))
			.andExpect(jsonPath("$.scheduleId").value("1"))
			.andExpect(jsonPath("$.name").value("testSchedule111"))	
			
			.andExpect(jsonPath("$.users[0].userId").value("1"))
			.andExpect(jsonPath("$.users[0].nickName").value("test11"))
		
			.andExpect(jsonPath("$.users[1].userId").value("2"))
			.andExpect(jsonPath("$.users[1].nickName").value("test22"))
			
			.andExpect(jsonPath("$.users[2].userId").value("3"))
			.andExpect(jsonPath("$.users[2].nickName").value("test33"))
		
			.andExpect(jsonPath("$.users[3]").doesNotExist())
			
			.andExpect(jsonPath("$.startDate").value("20240430:23:59"))
			.andExpect(jsonPath("$.endDate").value("20240501:00:00"))
			.andExpect(jsonPath("$.content").value("hihi"))
			.andExpect(jsonPath("$.state").value("DONE"))
					   
			.andExpect(jsonPath("$.chatList[0].chatId").value("1"))
			.andExpect(jsonPath("$.chatList[0].userId").value("1"))
			.andExpect(jsonPath("$.chatList[0].nickName").value("test11"))
			.andExpect(jsonPath("$.chatList[0].content").value("test"))
			.andExpect(jsonPath("$.chatList[0].createDate").value("20240401:00:00:00"))
					   
			.andExpect(jsonPath("$.chatList[1].chatId").value("2"))
			.andExpect(jsonPath("$.chatList[1].userId").value("2"))
			.andExpect(jsonPath("$.chatList[1].nickName").value("test22"))
			.andExpect(jsonPath("$.chatList[1].content").value("test"))
			.andExpect(jsonPath("$.chatList[1].createDate").value("20240401:00:00:00"))

			.andExpect(jsonPath("$.chatList[2]").doesNotExist());
    }
	
	// @Test // !!!여기 통과하도록 로직 수정해야함!!!
	// public void 스케줄_수정_권한없음() throws Exception {
	// //given
	// String request = "{ \"name\" : \"testSchedule111\" ,"
	// 				+ " \"users\" : [1, 2, 3] ,"  
	// + " \"startDate\" : \"20240430:23:59\" ,"
	// 				+ " \"endDate\" : \"20240501:00:00\" ," 
	// 				+ " \"content\" : \"hihi\" ,"
	// 				+ " \"state\" : \"DONE\" }";
	// //when
	// mvc.perform(put("/v1/schedules/1")
	// 	.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PENDING)
	// .contentType(MediaType.APPLICATION_JSON)
	// 	.content(request))
	// //then
	// 	.andExpect(status().isForbidden());
	// }
	
	@Test 
	public void 스케줄_삭제() throws Exception {
	//given
    //when
    mvc.perform(delete("/v1/schedules/1")
		.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER))
	//then
		.andExpect(status().isOk());
		
	mvc.perform(get("/v1/schedules/1")
		.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER))
		.andExpect(status().isNotFound());
	}
	
	// @Test //!!!여기 통과하도록 로직 수정해야함!!!
	// public void 스케줄_삭제_권한없음() throws Exception {
	// //given
	// //when
	// mvc.perform(delete("/v1/schedules/1")
	// 	.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER_PENDING))
	// //then
	// 	.andExpect(status().isForbidden());
	// }
	
	@Test
    public void 스케줄_상태_변경() throws Exception {
        //given
        String request = "{ \"state\" : \"DONE\" }";
        //when
        mvc.perform(put("/v1/schedules/1/state")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER)
    		.contentType(MediaType.APPLICATION_JSON)
			.content(request))
        //then
            .andExpect(status().isOk())
			
			.andExpect(jsonPath("$.workspaceId").value("1"))
			.andExpect(jsonPath("$.workspaceName").value("testWorkspace1"))
			.andExpect(jsonPath("$.scheduleId").value("1"))
			.andExpect(jsonPath("$.name").value("testSchedule1"))	
			
			.andExpect(jsonPath("$.users[0].userId").value("1"))
			.andExpect(jsonPath("$.users[0].nickName").value("test11"))
		
			.andExpect(jsonPath("$.users[1].userId").value("2"))
			.andExpect(jsonPath("$.users[1].nickName").value("test22"))
		
			.andExpect(jsonPath("$.users[2]").doesNotExist())
			
			.andExpect(jsonPath("$.startDate").value("20240401:00:00"))
			.andExpect(jsonPath("$.endDate").value("20240430:23:59"))
			.andExpect(jsonPath("$.content").value("hihi"))
			.andExpect(jsonPath("$.state").value("DONE"))
					   
			.andExpect(jsonPath("$.chatList[0].chatId").value("1"))
			.andExpect(jsonPath("$.chatList[0].userId").value("1"))
			.andExpect(jsonPath("$.chatList[0].nickName").value("test11"))
			.andExpect(jsonPath("$.chatList[0].content").value("test"))
			.andExpect(jsonPath("$.chatList[0].createDate").value("20240401:00:00:00"))
					   
			.andExpect(jsonPath("$.chatList[1].chatId").value("2"))
			.andExpect(jsonPath("$.chatList[1].userId").value("2"))
			.andExpect(jsonPath("$.chatList[1].nickName").value("test22"))
			.andExpect(jsonPath("$.chatList[1].content").value("test"))
			.andExpect(jsonPath("$.chatList[1].createDate").value("20240401:00:00:00"))

			.andExpect(jsonPath("$.chatList[2]").doesNotExist());
    }
	
	// @Test //!!!여기 통과하도록 로직 수정해야함!!!
	// public void 스케줄_상태_변경_권한없음() throws Exception {
	// //given
	// String request = "{ \"state\" : \"DONE\" }";
	// //when
	// mvc.perform(put("/v1/schedules/1/state")
	// 	.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PENDING)
	// .contentType(MediaType.APPLICATION_JSON)
	// 	.content(request))
	// //then
	// 	.andExpect(status().isForbidden());
	// }
	
	@Test
    public void 스케줄_댓글_추가() throws Exception {
        //given
        String request = "{ \"content\" : \"hello\" }";
        //when
        mvc.perform(post("/v1/schedules/1/chat")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER)
    		.contentType(MediaType.APPLICATION_JSON)
			.content(request))
        //then
            .andExpect(status().isCreated())

			.andExpect(jsonPath("$.chatId").exists())
			.andExpect(jsonPath("$.scheduleId").value("1"))
			.andExpect(jsonPath("$.nickName").value("test22"))
			.andExpect(jsonPath("$.content").value("hello"));
    }
	// @Test //!!!여기 통과하도록 로직 수정해야함!!!
	// public void 스케줄_채팅_추가_권한없음() throws Exception {
	// //given
	// String request = "{ \"content\" : \"hello\" }";
	// //when
	// mvc.perform(post("/v1/schedules/1/chat")
	// 		.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PENDING)
	// .contentType(MediaType.APPLICATION_JSON)
	// 		.content(request))
	// 	//then
	// 		.andExpect(status().isForbidden());
	// }
	
	@Test
    public void 스케줄_댓글_수정() throws Exception {
        //given
        String request = "{ \"content\" : \"hello\" }";
        //when
        mvc.perform(put("/v1/schedules/1/chat/1")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
    		.contentType(MediaType.APPLICATION_JSON)
			.content(request))
        //then
            .andExpect(status().isOk())

			.andExpect(jsonPath("$.chatId").value("1"))
			.andExpect(jsonPath("$.scheduleId").value("1"))
			.andExpect(jsonPath("$.nickName").value("test11"))
			.andExpect(jsonPath("$.content").value("hello"));
    }
	
	@Test //!!!여기 통과하도록 로직 수정해야함!!!
	public void 스케줄_채팅_수정_권한없음() throws Exception {
	//given
	String request = "{ \"content\" : \"hello\" }";
	//when
	mvc.perform(put("/v1/schedules/1/chat/1")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER)
			.contentType(MediaType.APPLICATION_JSON)
			.content(request))
		//then
			.andExpect(status().isForbidden());
	}
	
	@Test
    public void 스케줄_댓글_삭제() throws Exception {
        //given
        //when
        mvc.perform(delete("/v1/schedules/1/chat/1")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN))
        //then
            .andExpect(status().isOk());
    }
	
	// @Test //!!!여기 통과하도록 로직 수정해야함!!!
	// public void 스케줄_채팅_삭제_권한없음() throws Exception {
	// //given
	// //when
	// mvc.perform(put("/v1/schedules/1/chat/1")
	// 		.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER))
	// 	//then
	// 		.andExpect(status().isForbidden());
	// }
}