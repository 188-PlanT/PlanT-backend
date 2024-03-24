package project.common;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

import org.junit.jupiter.api.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
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
}