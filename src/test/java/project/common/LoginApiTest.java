package project.common;

import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

import org.junit.jupiter.api.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

public class LoginApiTest extends IntegrationTest {
	
	@Test //localTestDB에는 암호화 안된 비밀번호 입력됨 -> dumyLogin 사용해야함
	public void 이메일_로그인() throws Exception {
		//given
		String request = "{ \"email\" : \"test1@gmail.com\" , \"password\" : \"test1234\" }";

		//when
		mvc.perform(post("/v1/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(request))
			//then
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.accessToken").exists())
			.andExpect(jsonPath("$.refreshToken").exists());
	}
	
	@Test 
	public void 리프레시_토큰_재발급() throws Exception {
		//given
		//when
		mvc.perform(post("/v1/refresh")
			.header("Refresh-Token", REFRESH_TOKEN))
		//then
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.accessToken").exists());
	}
	
	@Test
	public void 회원가입() throws Exception {
		//given
		String request = "{ \"email\" : \"test5@gmail.com\" , \"password\" : \"test1234\" }";
		
		//when
		mvc.perform(post("/v1/sign-up")
			.contentType(MediaType.APPLICATION_JSON)
			.content(request))
		//then
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.userId").exists())
			.andExpect(jsonPath("$.email").value("test5@gmail.com"));
	}
}