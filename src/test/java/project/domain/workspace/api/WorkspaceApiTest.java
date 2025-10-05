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
}
