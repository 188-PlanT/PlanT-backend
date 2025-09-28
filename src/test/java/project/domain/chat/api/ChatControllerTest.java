package project.domain.chat.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import project.common.helper.ApiIntegrationTest;

public class ChatControllerTest extends ApiIntegrationTest {

    @Test
    public void 댓글_추가() throws Exception {
        // given
        String request = "{ \"scheduleId\" : 1, \"content\" : \"hello\" }";
        // when
        mvc.perform(post("/v1/chats")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                // then
                .andExpect(status().isOk());
    }

    @Test
    public void 댓글_수정() throws Exception {
        // given
        String request = "{ \"content\" : \"hello\" }";
        // when
        mvc.perform(put("/v1/chats/1")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                // then
                .andExpect(status().isOk());
    }

    @Test
    public void 댓글_수정_권한없음() throws Exception {
        // given
        String request = "{ \"content\" : \"hello\" }";
        // when
        mvc.perform(put("/v1/chats/1")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                // then
                .andExpect(status().isConflict());
    }

    @Test
    public void 댓글_삭제() throws Exception {
        // given
        // when
        mvc.perform(delete("/v1/chats/1").header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_ADMIN))
                // then
                .andExpect(status().isOk());
    }

    @Test
    public void 댓글_삭제_권한없음() throws Exception {
        // given
        // when
        mvc.perform(delete("/v1/chats/1").header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_USER))
                // then
                .andExpect(status().isConflict());
    }
}
