package project.common;

import project.domain.user.domain.User;
import project.common.util.UserUtil;
import project.common.security.jwt.JwtProvider;

import javax.annotation.PostConstruct;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
@Transactional
public class IntegrationTest {
	@Autowired
	private UserUtil userUtil;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	protected MockMvc mvc;
	
	protected String ACCESS_TOKEN;
	protected String ACCESS_TOKEN_USER;
	protected String ACCESS_TOKEN_OUTSIDER;
	protected String REFRESH_TOKEN;
	protected String ACCESS_TOKEN_NO_NICKNAME;
	
	@PostConstruct
	private void setAccessToken() {
		User user = userUtil.getUserById(1L);

		ACCESS_TOKEN = "Bearer " + jwtProvider.createAccessTokenByUser(user);
		ACCESS_TOKEN_USER = "Bearer " + jwtProvider.createAccessTokenByUser(userUtil.getUserById(2L));
		ACCESS_TOKEN_OUTSIDER = "Bearer " + jwtProvider.createAccessTokenByUser(userUtil.getUserById(3L));
		ACCESS_TOKEN_NO_NICKNAME = "Bearer " + jwtProvider.createAccessTokenByUser(userUtil.getUserById(4L));
		REFRESH_TOKEN = "Bearer " + jwtProvider.createRefreshTokenByUser(user);
	}
}