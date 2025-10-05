package project.domain.user.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import project.common.helper.ApiIntegrationTest;

public class UserApiTest extends ApiIntegrationTest {

    @Test
    public void 이메일_검증() throws Exception {
        // given
        String request = "{ \"email\" : \"test1@gmail.com\" }";

        // when
        mvc.perform(post("/v1/users/email").content(request).contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value("false"));
    }

    // @Test
    // public void 이메일_인증() throws Exception {
    // 	//given
    // 	String email = "kimsh8580@gmail.com";
    // 	//when
    // 	mvc.perform(get("/v1/users/email/code?email=" + email))
    // 	//then
    // 	.andExpect(status().isOk());
    // }

    @Test
    public void 닉네임_검증() throws Exception {
        // given
        String request = "{ \"nickName\" : \"test44\" }";

        // when
        mvc.perform(post("/v1/users/nickname")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_NO_NICKNAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value("true"));
    }

    @Test
    public void 닉네임_변경() throws Exception {
        // given
        String request = "{ \"nickName\" : \"test44\" }";

        // when
        mvc.perform(put("/v1/users/nickname")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_NO_NICKNAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    public void 유저_정보_조회() throws Exception {
        // given
        // when
        mvc.perform(get("/v1/users/me").header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_ADMIN))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.nickName").value("test11"))
                .andExpect(jsonPath("$.email").value("test1@gmail.com"))
                .andExpect(jsonPath("$.profile").value("https://d12v02yfguudwt.cloudfront.net/user.png"))
                .andExpect(jsonPath("$.state").value("ADMIN"));
    }

    @Test
    public void 유저_정보_수정() throws Exception {
        // given
        String request = "{ \"nickName\" : \"test111\" ," + "\"currentPassword\" : \"test1234\" , "
                + "\"newPassword\" : \"test4321\" , "
                + "\"profile\" : \"https://d12v02yfguudwt.cloudfront.net/user.png\" }";

        // when
        mvc.perform(put("/v1/users/me")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                // then
                .andExpect(status().isOk());
    }

    @Test
    public void 유저_정보_수정_옵셔널() throws Exception {
        // given
        String request = " { \"currentPassword\" : \"test1234\" , "
                + "\"newPassword\" : \"test4321\" , "
                + "\"profile\" : \"https://d12v02yfguudwt.cloudfront.net/user.png\" }";

        // when
        mvc.perform(put("/v1/users/me")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                // then
                .andExpect(status().isOk());
    }

    @Test
    public void 유저_정보_수정_비밀번호_오류() throws Exception {
        // given
        String request = " { \"currentPassword\" : \"test5555\" , "
                + "\"newPassword\" : \"test4321\" , "
                + "\"profile\" : \"https://d12v02yfguudwt.cloudfront.net/user.png\" }";

        // when
        mvc.perform(put("/v1/users/me")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                // then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorName").value("USER_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("비밀번호가 올바르지 않습니다"));
    }

    @Test
    public void 워크스페이스_조회() throws Exception {
        // given
        // when
        mvc.perform(get("/v1/users/me/workspaces").header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_ADMIN))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.workspaceUsers[0].workspaceId").value("1"))
                .andExpect(jsonPath("$.workspaceUsers[0].role").value("ADMIN"))
                .andExpect(jsonPath("$.workspaceUsers[1].workspaceId").value("2"))
                .andExpect(jsonPath("$.workspaceUsers[1].role").value("ADMIN"));
    }

    @Test
    public void 스케줄_조회_시작_겹치게() throws Exception {
        // given
        LocalDateTime startDate = LocalDateTime.of(2024, 4, 30, 23, 59, 59);
        LocalDateTime endDate = LocalDateTime.of(2024, 5, 31, 23, 59, 59);

        // when
        mvc.perform(get("/v1/users/me/schedules")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_ADMIN)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.schedules.toDo[0].scheduleId").value("1"))
                .andExpect(jsonPath("$.schedules.toDo[0].workspaceId").value("1"))
                .andExpect(jsonPath("$.schedules.toDo[0].workspaceName").value("testWorkspace1"))
                .andExpect(jsonPath("$.schedules.toDo[0].scheduleName").value("testSchedule1"))
                .andExpect(jsonPath("$.schedules.toDo[0].endDate").value("2024-04-30T23:59:59"))
                .andExpect(jsonPath("$.schedules.toDo[1].scheduleId").value("2"))
                .andExpect(jsonPath("$.schedules.toDo[1].workspaceId").value("1"))
                .andExpect(jsonPath("$.schedules.toDo[1].workspaceName").value("testWorkspace1"))
                .andExpect(jsonPath("$.schedules.toDo[1].scheduleName").value("testSchedule2"))
                .andExpect(jsonPath("$.schedules.toDo[1].endDate").value("2024-05-01T00:00:00"))
                .andExpect(jsonPath("$.schedules.toDo[2]").doesNotExist())
                .andExpect(jsonPath("$.schedules.inProgress[0]").doesNotExist())
                .andExpect(jsonPath("$.schedules.done[0]").doesNotExist());
    }

    @Test
    public void 스케줄_조회_끝_겹치게() throws Exception {
        // given
        LocalDateTime startDate = LocalDateTime.of(2024, 5, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 5, 31, 23, 59, 59);

        // when
        mvc.perform(get("/v1/users/me/schedules")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_ADMIN)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.schedules.toDo[0].scheduleId").value("2"))
                .andExpect(jsonPath("$.schedules.toDo[0].workspaceId").value("1"))
                .andExpect(jsonPath("$.schedules.toDo[0].workspaceName").value("testWorkspace1"))
                .andExpect(jsonPath("$.schedules.toDo[0].scheduleName").value("testSchedule2"))
                .andExpect(jsonPath("$.schedules.toDo[0].endDate").value("2024-05-01T00:00:00"))
                .andExpect(jsonPath("$.schedules.toDo[1]").doesNotExist())
                .andExpect(jsonPath("$.schedules.inProgress[0]").doesNotExist())
                .andExpect(jsonPath("$.schedules.done[0]").doesNotExist());
    }

    @Test
    public void 유저_검색() throws Exception {
        // given
        String keyword = "test";
        // when
        mvc.perform(get("/v1/users/search?keyword=" + keyword).header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_ADMIN))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].userId").value("2"))
                .andExpect(jsonPath("$.users[0].nickName").value("test22"))
                .andExpect(jsonPath("$.users[0].email").value("test2@gmail.com"))
                .andExpect(jsonPath("$.users[1].userId").value("3"))
                .andExpect(jsonPath("$.users[1].nickName").value("test33"))
                .andExpect(jsonPath("$.users[1].email").value("test3@gmail.com"))
                .andExpect(jsonPath("$.users[2]").doesNotExist());
    }
}
