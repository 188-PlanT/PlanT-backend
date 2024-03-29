package project.common;

import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

import org.junit.jupiter.api.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WorkspaceApiTest extends IntegrationTest {
	
	@Test
    public void 워크스페이스_생성() throws Exception {
        //given
        String request = "{ \"name\" : \"testWorkspace3\" , \"users\" : [2,3] }";

        //when
        mvc.perform(post("/v1/workspaces")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
    		.contentType(MediaType.APPLICATION_JSON)
			.content(request))
        //then
            .andExpect(status().isOk());
    }
	
	@Test
    public void 워크스페이스_수정() throws Exception {
        //given
        String request = "{ \"name\" : \"testWorkspace11\" , \"profile\" : \"https://plant-s3.s3.ap-northeast-2.amazonaws.com/user.png\" }";

        //when
        mvc.perform(put("/v1/workspaces/1")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
    		.contentType(MediaType.APPLICATION_JSON)
			.content(request))
        //then
            .andExpect(status().isOk())
			.andExpect(jsonPath("$.workspaceId").value("1"))
			.andExpect(jsonPath("$.name").value("testWorkspace11"))
			.andExpect(jsonPath("$.profile").value("https://plant-s3.s3.ap-northeast-2.amazonaws.com/user.png"));
    }
	
	@Test // admin 권한이 없는 유저가 워크스페이스 수정 시도시 forbidden 에러 응답 반환
    public void 워크스페이스_수정_일반_유저_권한() throws Exception {
        //given
        String request = "{ \"name\" : \"testWorkspace11\" , \"profile\" : \"https://plant-s3.s3.ap-northeast-2.amazonaws.com/user.png\" }";

        //when
        mvc.perform(put("/v1/workspaces/1")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER)
    		.contentType(MediaType.APPLICATION_JSON)
			.content(request))
        //then
            .andExpect(status().isForbidden());
    }
	
	@Test
    public void 워크스페이스_삭제() throws Exception {
        //given
        //when
        mvc.perform(delete("/v1/workspaces/1")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN))
        //then
			.andExpect(status().isOk());
		
		mvc.perform(delete("/v1/workspaces/1")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN))
            .andExpect(status().isNotFound());
    }
	
	@Test // admin 권한이 없는 유저가 워크스페이스 삭제 시도시 forbidden 에러 응답 반환
    public void 워크스페이스_삭제_일반_유저_권한() throws Exception {
        //given
        //when
        mvc.perform(delete("/v1/workspaces/1")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER))
        //then
			.andExpect(status().isForbidden());
    }
	
	@Test
    public void 워크스페이스_유저_조회() throws Exception {
        //given
        //when
        mvc.perform(get("/v1/workspaces/1/users")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER))
        //then
            .andExpect(status().isOk())
			
			.andExpect(jsonPath("$.workspaceId").value("1"))
			.andExpect(jsonPath("$.workspaceName").value("testWorkspace1"))
			.andExpect(jsonPath("$.profile").value("https://plant-s3.s3.ap-northeast-2.amazonaws.com/workspace.png"))
			
			.andExpect(jsonPath("$.users[0].userId").value("1"))
			.andExpect(jsonPath("$.users[0].nickName").value("test11"))
			.andExpect(jsonPath("$.users[0].email").value("test1@gmail.com"))
			.andExpect(jsonPath("$.users[0].authority").value("ADMIN"))
			
			.andExpect(jsonPath("$.users[1].userId").value("2"))
			.andExpect(jsonPath("$.users[1].nickName").value("test22"))
			.andExpect(jsonPath("$.users[1].email").value("test2@gmail.com"))
			.andExpect(jsonPath("$.users[1].authority").value("USER"))
			
			.andExpect(jsonPath("$.users[2].userId").value("3"))
			.andExpect(jsonPath("$.users[2].nickName").value("test33"))
			.andExpect(jsonPath("$.users[2].email").value("test3@gmail.com"))
			.andExpect(jsonPath("$.users[2].authority").value("PENDING"))
			
			.andExpect(jsonPath("$.users[3]").doesNotExist());
    }
	
	@Test // 가입대기 중 유저가 워크스페이스 유저 조회시 권한 없음
    public void 워크스페이스_유저_조회_권한없음() throws Exception {
        //given
        //when
        mvc.perform(get("/v1/workspaces/1/users")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PENDING))
        //then
            .andExpect(status().isForbidden());
    }
	
	// @Test // 이메일 로직 반복으로 비활성화
	// public void 워크스페이스_유저_초대() throws Exception {
	// //given
	// String request = "{ \"userId\" : \"5\" }";
	// //when
	// mvc.perform(post("/v1/workspaces/1/users")
	// 		.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
	// 		.contentType(MediaType.APPLICATION_JSON)
	// 		.content(request))
	// //then
	// .andExpect(status().isOk())
			
	// 		.andExpect(jsonPath("$.workspaceId").value("1"))
	// 		.andExpect(jsonPath("$.workspaceName").value("testWorkspace1"))
	// 		.andExpect(jsonPath("$.profile").value("https://plant-s3.s3.ap-northeast-2.amazonaws.com/workspace.png"))
			
	// 		.andExpect(jsonPath("$.users[0].userId").value("1"))
	// 		.andExpect(jsonPath("$.users[0].nickName").value("test11"))
	// 		.andExpect(jsonPath("$.users[0].email").value("test1@gmail.com"))
	// 		.andExpect(jsonPath("$.users[0].authority").value("ADMIN"))
			
	// 		.andExpect(jsonPath("$.users[1].userId").value("2"))
	// 		.andExpect(jsonPath("$.users[1].nickName").value("test22"))
	// 		.andExpect(jsonPath("$.users[1].email").value("test2@gmail.com"))
	// 		.andExpect(jsonPath("$.users[1].authority").value("USER"))
			
	// 		.andExpect(jsonPath("$.users[2].userId").value("3"))
	// 		.andExpect(jsonPath("$.users[2].nickName").value("test33"))
	// 		.andExpect(jsonPath("$.users[2].email").value("test3@gmail.com"))
	// 		.andExpect(jsonPath("$.users[2].authority").value("PENDING"))
			
	// 		.andExpect(jsonPath("$.users[3].userId").value("5"))
	// 		.andExpect(jsonPath("$.users[3].nickName").value("kimsh8580"))
	// 		.andExpect(jsonPath("$.users[3].email").value("kimsh8580@gmail.com"))
	// 		.andExpect(jsonPath("$.users[3].authority").value("PENDING"))
			
	// 		.andExpect(jsonPath("$.users[4]").doesNotExist());
	// }
	
	@Test // 가입대기 중 유저가 워크스페이스 유저 조회시 권한 없음
    public void 워크스페이스_유저_초대_권한없음() throws Exception {
        //given
        String request = "{ \"userId\" : \"5\" }";
        //when
        mvc.perform(post("/v1/workspaces/1/users")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER)
			.contentType(MediaType.APPLICATION_JSON)
			.content(request))
        //then
            .andExpect(status().isForbidden());
    }
	
	@Test 
    public void 워크스페이스_유저_추방() throws Exception {
        //given
        //when
        mvc.perform(delete("/v1/workspaces/1/users/3")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN))
        //then
            .andExpect(status().isOk());
		
		// 추방 시 워크스페이스 유저 목록에서 유저 사라져야함
		mvc.perform(get("/v1/workspaces/1/users")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.users[0].userId").value("1"))
			.andExpect(jsonPath("$.users[1].userId").value("2"))
			.andExpect(jsonPath("$.users[2]").doesNotExist());
    }
	
	@Test // ADMIN이 1명 뿐인 워크스페이스에서 ADMIN이 탈퇴시 404 에러 발생
    public void 워크스페이스_유일_방장_추방() throws Exception {
        //given
        //when
        mvc.perform(delete("/v1/workspaces/1/users/1")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN))
        //then
            .andExpect(status().isNotFound());
		
		// 추방 시 워크스페이스 유저 목록에서 유저 사라져야함
		mvc.perform(get("/v1/workspaces/1/users")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.users[0].userId").value("1"));
    }
	
	@Test // ADMIN이 아닌 유저가 유저 추방시 권한 없음
    public void 워크스페이스_유저_추방_권한없음() throws Exception {
        //given
        //when
        mvc.perform(delete("/v1/workspaces/1/users/3")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER))
        //then
            .andExpect(status().isForbidden());
		
		// 추방 시 워크스페이스 유저 목록에서 유저 사라져야함
		mvc.perform(get("/v1/workspaces/1/users")
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.users[2].userId").value("3"));
    }
	
	@Test 
    public void 워크스페이스_캘린더_조회() throws Exception {
        //given
		String date = "202404";
        //when
        mvc.perform(get("/v1/workspaces/1/calendar?date=" + date)
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER))
        //then
            .andExpect(status().isOk())
			.andExpect(jsonPath("$.workspaceId").value("1"))
			.andExpect(jsonPath("$.workspaceName").value("testWorkspace1"))
			.andExpect(jsonPath("$.role").value("USER"))
				
			.andExpect(jsonPath("$.schedules[0].scheduleId").value("1"))
			.andExpect(jsonPath("$.schedules[0].scheduleName").value("testSchedule1"))
			.andExpect(jsonPath("$.schedules[0].startDate").value("20240401"))
			.andExpect(jsonPath("$.schedules[0].endDate").value("20240430"))
			.andExpect(jsonPath("$.schedules[0].state").value("TODO"))
				
			.andExpect(jsonPath("$.schedules[1].scheduleId").value("2"))
			.andExpect(jsonPath("$.schedules[1].scheduleName").value("testSchedule2"))
			.andExpect(jsonPath("$.schedules[1].startDate").value("20240430"))
			.andExpect(jsonPath("$.schedules[1].endDate").value("20240501"))
			.andExpect(jsonPath("$.schedules[1].state").value("TODO"))
				
			.andExpect(jsonPath("$.schedules[2]").doesNotExist());
	}
	
	@Test // 가입대기 유저가 캘린더 조회시 권한 없음
    public void 워크스페이스_캘린더_조회_권한없음() throws Exception {
        //given
		String date = "202404";
        //when
        mvc.perform(get("/v1/workspaces/1/calendar?date=" + date)
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PENDING))
        //then
            .andExpect(status().isForbidden());
    }
	
	@Test 
    public void 워크스페이스별_스케줄_조회() throws Exception {
        //given
		String date = "20240401";
        //when
        mvc.perform(get("/v1/workspaces/1/schedules?date=" + date)
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER))
        //then
            .andExpect(status().isOk())
			.andExpect(jsonPath("$.workspaceId").value("1"))
			.andExpect(jsonPath("$.workspaceName").value("testWorkspace1"))
			.andExpect(jsonPath("$.role").value("USER"))
				
			.andExpect(jsonPath("$.schedules[0].scheduleId").value("1"))
			.andExpect(jsonPath("$.schedules[0].scheduleName").value("testSchedule1"))
			.andExpect(jsonPath("$.schedules[0].startDate").value("20240401"))
			.andExpect(jsonPath("$.schedules[0].endDate").value("20240430"))
			.andExpect(jsonPath("$.schedules[0].state").value("TODO"))
				
			.andExpect(jsonPath("$.schedules[1]").doesNotExist());
	}
	
	@Test // 가입대기 유저가 스케줄 조회시 권한 없음
    public void 워크스페이스_스케줄_조회_권한없음() throws Exception {
        //given
		String date = "20240401";
        //when
        mvc.perform(get("/v1/workspaces/1/schedules?date=" + date)
			.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PENDING))
        //then
            .andExpect(status().isForbidden());
    }
}