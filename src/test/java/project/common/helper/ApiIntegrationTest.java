package project.common.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import project.common.security.jwt.JwtProvider;
import project.common.util.UserUtil;
import project.domain.user.domain.User;

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ApiIntegrationTest {
    @Autowired
    private UserUtil userUtil;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected String ACCESS_TOKEN_ADMIN;
    protected String ACCESS_TOKEN_USER;
    protected String ACCESS_TOKEN_OUTSIDER;
    protected String REFRESH_TOKEN;
    protected String ACCESS_TOKEN_NO_NICKNAME;

    @PostConstruct
    private void setAccessToken() {
        User user = userUtil.getUserById(1L);

        ACCESS_TOKEN_ADMIN = "Bearer " + jwtProvider.createAccessTokenByUser(user);
        ACCESS_TOKEN_USER = "Bearer " + jwtProvider.createAccessTokenByUser(userUtil.getUserById(2L));
        ACCESS_TOKEN_OUTSIDER = "Bearer " + jwtProvider.createAccessTokenByUser(userUtil.getUserById(3L));
        ACCESS_TOKEN_NO_NICKNAME = "Bearer " + jwtProvider.createAccessTokenByUser(userUtil.getUserById(4L));
        REFRESH_TOKEN = "Bearer " + jwtProvider.createRefreshTokenByUser(user);
    }
}
