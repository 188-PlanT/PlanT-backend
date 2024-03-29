package project.common;

import project.domain.*;
import project.service.*;
import project.dto.login.SignUpRequest;
import project.common.auth.jwt.JwtProvider;

import javax.annotation.PostConstruct;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
@Transactional
public class IntegrationTest {
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	protected MockMvc mvc;
	
	protected String ACCESS_TOKEN;
	protected String ACCESS_TOKEN_USER;
	protected String ACCESS_TOKEN_PENDING;
	protected String REFRESH_TOKEN;
	protected String ACCESS_TOKEN_NO_NICKNAME;
	
	@PostConstruct
	private void setAccessToken() {
		User user = userService.findByEmail("test1@gmail.com");
		ACCESS_TOKEN = "Bearer " + jwtProvider.createAccessToken(user);
		ACCESS_TOKEN_USER = "Bearer " + jwtProvider.createAccessToken(userService.findByEmail("test2@gmail.com"));
		ACCESS_TOKEN_PENDING = "Bearer " + jwtProvider.createAccessToken(userService.findByEmail("test3@gmail.com"));
		ACCESS_TOKEN_NO_NICKNAME = "Bearer " + jwtProvider.createAccessToken(userService.findByEmail("test4@gmail.com"));
		REFRESH_TOKEN = "Bearer " + jwtProvider.createRefreshToken(user);
	}
}