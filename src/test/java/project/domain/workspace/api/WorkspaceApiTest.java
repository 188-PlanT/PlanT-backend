package project.domain.workspace.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static project.common.constant.ImageConstant.PROFILE_URL;
import static project.common.constant.WorkspaceConstant.WORKSPACE_NAME;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import project.common.helper.ApiIntegrationTest;
import project.domain.workspace.dto.request.WorkspaceCreateRequest;
import project.domain.workspace.dto.request.WorkspaceUpdateRequest;

public class WorkspaceApiTest extends ApiIntegrationTest {

    @Test
    public void 워크스페이스_생성() throws Exception {
        // given
        var request = new WorkspaceCreateRequest(WORKSPACE_NAME);
        var json = objectMapper.writeValueAsString(request);

        // when
        mvc.perform(post("/v1/workspaces")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    public void 워크스페이스_수정() throws Exception {
        // given
        var request = new WorkspaceUpdateRequest(WORKSPACE_NAME, PROFILE_URL);
        var json = objectMapper.writeValueAsString(request);

        // when
        mvc.perform(put("/v1/workspaces/1")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // then
                .andExpect(status().isOk());
    }

    @Test // admin 권한이 없는 유저가 워크스페이스 수정 시도시 forbidden 에러 응답 반환
    public void 워크스페이스_수정_일반_유저_권한() throws Exception {
        // given
        var request = new WorkspaceUpdateRequest(WORKSPACE_NAME, PROFILE_URL);
        var json = objectMapper.writeValueAsString(request);

        // when
        mvc.perform(put("/v1/workspaces/1")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER) // 일반 유저 토큰
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    public void 워크스페이스_삭제() throws Exception {
        // given
        // when
        mvc.perform(delete("/v1/workspaces/1").header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_ADMIN))
                // then
                .andExpect(status().isOk());
    }

    @Test // admin 권한이 없는 유저가 워크스페이스 삭제 시도시 forbidden 에러 응답 반환
    public void 워크스페이스_삭제_일반_유저_권한() throws Exception {
        // given
        // when
        mvc.perform(delete("/v1/workspaces/1").header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    public void 워크스페이스_캘린더_조회() throws Exception {
        // given
        String date = "202404";
        // when
        mvc.perform(get("/v1/workspaces/1/calendar?date=" + date).header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceId").value("1"))
                .andExpect(jsonPath("$.workspaceName").value("testWorkspace1"))
                .andExpect(jsonPath("$.schedules[0].scheduleId").value("1"))
                .andExpect(jsonPath("$.schedules[0].name").value("testSchedule1"))
                .andExpect(jsonPath("$.schedules[1].scheduleId").value("2"))
                .andExpect(jsonPath("$.schedules[1].name").value("testSchedule2"))
                .andExpect(jsonPath("$.schedules[2]").doesNotExist());
    }

    @Test // 외부 유저가 캘린더 조회시 권한 없음
    public void 워크스페이스_캘린더_조회_외부인() throws Exception {
        // given
        String date = "202404";
        // when
        mvc.perform(get("/v1/workspaces/1/calendar?date=" + date)
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_OUTSIDER))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    public void 워크스페이스별_스케줄_조회_시작날짜() throws Exception {
        // given
        String date = "20240401";
        // when
        mvc.perform(get("/v1/workspaces/1/schedules?date=" + date).header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceId").value("1"))
                .andExpect(jsonPath("$.workspaceName").value("testWorkspace1"))
                .andExpect(jsonPath("$.schedules[0].scheduleId").value("1"))
                .andExpect(jsonPath("$.schedules[0].name").value("testSchedule1"))
                .andExpect(jsonPath("$.schedules[1]").doesNotExist());
    }

    @Test
    public void 워크스페이스별_스케줄_조회_끝날짜() throws Exception {
        // given
        String date = "20240430";
        // when
        mvc.perform(get("/v1/workspaces/1/schedules?date=" + date).header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceId").value("1"))
                .andExpect(jsonPath("$.workspaceName").value("testWorkspace1"))
                .andExpect(jsonPath("$.schedules[0].scheduleId").value("1"))
                .andExpect(jsonPath("$.schedules[0].name").value("testSchedule1"))
                .andExpect(jsonPath("$.schedules[1].scheduleId").value("2"))
                .andExpect(jsonPath("$.schedules[1].name").value("testSchedule2"))
                .andExpect(jsonPath("$.schedules[2]").doesNotExist());
    }

    @Test
    public void 워크스페이스별_스케줄_조회_중간날짜() throws Exception {
        // given
        String date = "20240405";
        // when
        mvc.perform(get("/v1/workspaces/1/schedules?date=" + date).header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceId").value("1"))
                .andExpect(jsonPath("$.workspaceName").value("testWorkspace1"))
                .andExpect(jsonPath("$.schedules[0].scheduleId").value("1"))
                .andExpect(jsonPath("$.schedules[0].name").value("testSchedule1"))
                .andExpect(jsonPath("$.schedules[1]").doesNotExist());
    }

    @Test // 외부 유저가 스케줄 조회시 권한 없음
    public void 워크스페이스_스케줄_조회_권한없음() throws Exception {
        // given
        String date = "20240401";
        // when
        mvc.perform(get("/v1/workspaces/1/schedules?date=" + date)
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_OUTSIDER))
                // then
                .andExpect(status().isForbidden());
    }
}
