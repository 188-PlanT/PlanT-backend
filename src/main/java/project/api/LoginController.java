package project.api;

import project.domain.*;
import project.dto.login.*;
import project.service.UserService;
import project.service.EmailService;
import project.common.auth.jwt.JwtProvider;
import project.exception.auth.*;
import project.common.auth.oauth.CustomOAuth2UserService;
import project.common.auth.oauth.OAuthAuthenticationProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import org.springframework.web.bind.annotation.*;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;


@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController{
    
    private final CustomOAuth2UserService customOAuth2UserService;
    private final EmailService emailService;
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final OAuthAuthenticationProvider oAuthAuthenticationProvider;
    
    //String으로 토큰 반환하기
    @PostMapping("/v1/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest request){ 
        User loginUser = userService.signIn(request.getEmail(), request.getPassword());

        String accessToken = jwtProvider.createAccessToken(loginUser);
        
        String refreshToken = jwtProvider.createRefreshToken(loginUser);
        
        ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                                                .httpOnly(true)
                                                .secure(true)
                                                .maxAge(60*60*24)
                                                .build();
        
        LoginResponse response = new LoginResponse(accessToken, refreshToken);

        return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, cookie.toString())
                            .body(response);
    }

    @PostMapping("/v1/refresh")
    public ResponseEntity<AccessTokenResponse> refreshToken(@RequestHeader("Refresh-Token") String refreshToken){
        
        String accessToken = jwtProvider.createAccessToken(refreshToken);
        
        ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                                                .httpOnly(true)
                                                .secure(true)
                                                .maxAge(60*60*24)
                                                .build();
        
        AccessTokenResponse response = new AccessTokenResponse(accessToken);
        
        return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, cookie.toString())
                            .body(response);
    }
    
    
    // <==유저 추가==>
    @PostMapping("/v1/sign-up")
    public ResponseEntity<SignUpResponse> registerUser(@Valid @RequestBody SignUpRequest request){
        
        User user = userService.register(request);
        
        SignUpResponse response = new SignUpResponse(user.getId(), user.getEmail());

        return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);    
    }
    
    @GetMapping("/noToken")
    public ResponseEntity<String> noTokenError(){
        throw new UnIdentifiedUserException("로그인이 필요한 요청입니다");    
    }
    
    @PostMapping("/v1/login/oauth2")
    public ResponseEntity<LoginResponse> oauth2Login(@RequestBody Oauth2LoginRequest request){
        User loginUser = customOAuth2UserService.getOAuth2User(request.getCode(), request.getProvider());
        
        String accessToken = jwtProvider.createAccessToken(loginUser);
        
        String refreshToken = jwtProvider.createRefreshToken(loginUser);
        
        LoginResponse response = new LoginResponse(accessToken, refreshToken);
            
        return ResponseEntity.ok(response);
    }
    
    @Getter
    @Setter
    static class Oauth2LoginRequest{
        private String code;
        private String provider;
    }
}