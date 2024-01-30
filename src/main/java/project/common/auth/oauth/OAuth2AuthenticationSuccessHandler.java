package project.common.auth.oauth;

import project.service.UserService;
import project.domain.User;
import project.common.auth.jwt.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import javax.servlet.ServletException;

import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.security.core.Authentication;


@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    
    private final String REDIRECT_URL = "https://reactstarter-hmait.run.goorm.site/login/redirect";
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserInfo userInfo = (UserInfo) authentication.getPrincipal();
        
        String accessToken = jwtProvider.createAccessToken(userInfo);
        
        String refreshToken = jwtProvider.createRefreshToken(userInfo);
        
        getRedirectStrategy().sendRedirect(request, response, getRedirectUrl(accessToken, refreshToken));
    }
    
    private String getRedirectUrl(String accessToken, String refreshToken){
        return UriComponentsBuilder.fromUriString(REDIRECT_URL)
                .queryParam("access_token", accessToken)
                .queryParam("refresh_token", refreshToken)
                .build().toUriString();
    }
}