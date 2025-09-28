package project.domain.schedule.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static project.common.constant.ScheduleConstant.*;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import project.common.helper.ApiIntegrationTest;
import project.domain.schedule.domain.Progress;
import project.domain.schedule.dto.request.ScheduleCreateRequest;
import project.domain.schedule.dto.request.ScheduleUpdateRequest;
import project.domain.schedule.dto.request.ScheduleUpdateStateRequest;

public class ScheduleApiTest extends ApiIntegrationTest {

    @Test
    public void 스케줄_생성() throws Exception {
        // given
        Long workspaceId = 1L;
        var request = new ScheduleCreateRequest(
                workspaceId, "testSchedule3", SCHEDULE_START_DATE, SCHEDULE_END_DATE, SCHEDULE_CONTENT);
        var requestJson = objectMapper.writeValueAsString(request);

        // when
        mvc.perform(post("/v1/schedules")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    public void 스케줄_생성_권한없음() throws Exception {
        // given
        Long workspaceId = 1L;
        var request = new ScheduleCreateRequest(
                workspaceId, "testSchedule3", SCHEDULE_START_DATE, SCHEDULE_END_DATE, SCHEDULE_CONTENT);
        var requestJson = objectMapper.writeValueAsString(request);
        // when
        mvc.perform(post("/v1/schedules")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_OUTSIDER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    public void 스케줄_상세_조회() throws Exception {
        // given
        // when
        mvc.perform(get("/v1/schedules/1").header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceId").value("1"))
                .andExpect(jsonPath("$.workspaceName").value("testWorkspace1"))
                .andExpect(jsonPath("$.scheduleId").value("1"))
                .andExpect(jsonPath("$.name").value("testSchedule1"))
                .andExpect(jsonPath("$.startDate").value("2024-04-01T00:00:00"))
                .andExpect(jsonPath("$.endDate").value("2024-04-30T23:59:59"))
                .andExpect(jsonPath("$.content").value("hihi"))
                .andExpect(jsonPath("$.state").value("TODO"))
                .andExpect(jsonPath("$.users[0].userId").value("1"))
                .andExpect(jsonPath("$.users[0].nickName").value("test11"))
                .andExpect(jsonPath("$.users[1].userId").value("2"))
                .andExpect(jsonPath("$.users[1].nickName").value("test22"))
                .andExpect(jsonPath("$.users[2]").doesNotExist())
                .andExpect(jsonPath("$.chatList[0].chatId").value("1"))
                .andExpect(jsonPath("$.chatList[0].userId").value("1"))
                .andExpect(jsonPath("$.chatList[0].nickName").value("test11"))
                .andExpect(jsonPath("$.chatList[0].content").value("test"))
                .andExpect(jsonPath("$.chatList[0].createDate").value("2024-04-01T00:00:00"))
                .andExpect(jsonPath("$.chatList[1].chatId").value("2"))
                .andExpect(jsonPath("$.chatList[1].userId").value("2"))
                .andExpect(jsonPath("$.chatList[1].nickName").value("test22"))
                .andExpect(jsonPath("$.chatList[1].content").value("test"))
                .andExpect(jsonPath("$.chatList[1].createDate").value("2024-04-01T00:00:00"))
                .andExpect(jsonPath("$.chatList[2]").doesNotExist());
    }

    @Test
    public void 스케줄_조회_권한없음() throws Exception {
        // given
        // when
        mvc.perform(get("/v1/schedules/1").header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_OUTSIDER))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    public void 스케줄_수정() throws Exception {
        // given
        var request = new ScheduleUpdateRequest(
                SCHEDULE_NAME, SCHEDULE_START_DATE, SCHEDULE_END_DATE, Progress.DONE, "updatedContent");
        var requestJson = objectMapper.writeValueAsString(request);

        // when
        mvc.perform(put("/v1/schedules/1")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                // then
                .andExpect(status().isOk());
    }

    @Test
    public void 스케줄_수정_권한없음() throws Exception {
        // given
        var request = new ScheduleUpdateRequest(
                SCHEDULE_NAME, SCHEDULE_START_DATE, SCHEDULE_END_DATE, Progress.DONE, "updatedContent");
        var requestJson = objectMapper.writeValueAsString(request);

        // when
        mvc.perform(put("/v1/schedules/1")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_OUTSIDER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    public void 스케줄_삭제() throws Exception {
        // given
        // when
        mvc.perform(delete("/v1/schedules/1").header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER))
                // then
                .andExpect(status().isOk());
    }

    @Test
    public void 스케줄_삭제_권한없음() throws Exception {
        // given
        // when
        mvc.perform(delete("/v1/schedules/1").header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_OUTSIDER))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    public void 스케줄_상태_변경() throws Exception {
        // given
        var request = new ScheduleUpdateStateRequest(Progress.DONE);
        var requestJson = objectMapper.writeValueAsString(request);

        // when
        mvc.perform(put("/v1/schedules/1/state")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                // then
                .andExpect(status().isOk());
    }

    @Test
    public void 스케줄_상태_변경_권한없음() throws Exception {
        // given
        var request = new ScheduleUpdateStateRequest(Progress.DONE);
        var requestJson = objectMapper.writeValueAsString(request);

        // when
        mvc.perform(put("/v1/schedules/1/state")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_OUTSIDER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                // then
                .andExpect(status().isForbidden());
    }
}
