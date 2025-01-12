package project.domain.auth.api;

import project.domain.auth.dto.request.LoginRequest;
import project.domain.auth.dto.request.Oauth2LoginRequest;
import project.domain.auth.dto.response.AccessTokenResponse;
import project.domain.auth.dto.response.LoginResponse;
import project.domain.auth.service.LoginService;
import project.domain.user.domain.User;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.domain.user.service.UserService;
import project.common.security.jwt.JwtProvider;
import project.domain.auth.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController{
    
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtProvider jwtProvider;
    private final LoginService loginService;
    
    //String으로 토큰 반환하기
    @PostMapping("/v1/login")
    public ResponseEntity<LoginResponse> loginUserByEmailAndPassword(@Valid @RequestBody LoginRequest request){
        LoginResponse response = loginService.loginByEmailAndPassword(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(response);
    }
    @PostMapping("/v1/login/oauth2")
    public ResponseEntity<LoginResponse> oauth2Login(@Valid @RequestBody Oauth2LoginRequest request){
        String loginUserEmail = customOAuth2UserService.getOauth2UserEmailByAuthCode(request.getCode(), request.getProvider());

        LoginResponse response = loginService.loginByOauth2UserEmail(loginUserEmail);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/v1/refresh")
    public ResponseEntity<AccessTokenResponse> loginByRefreshToken(@RequestHeader("Refresh-Token") String refreshToken){
        String accessToken = jwtProvider.createAccessTokenByRefreshToken(refreshToken);

        AccessTokenResponse response = new AccessTokenResponse(accessToken);

        return ResponseEntity.ok(response);
    }


    /**
     * SpringSecurityFilter에서 발생한 에러를 ControllerAdvice에서 공통으로 처리하기 위한 에러 핸들링 uri입니다.
     * JwtAuthenticationFilter -> CutomExceptionHandlerFilter -> /noToken (GET) -> controllerAdvice
     */
    @GetMapping("/noToken")
    public ResponseEntity<String> noTokenError(){
        throw new PlantException(ErrorCode.USER_UNAUTHORIZED);
    }

	// ### 더미 데이터 테스트용 로그인 기능 ###
	@PostMapping("/v1/login/dumy")
    public ResponseEntity<LoginResponse> dumyLogin(@Valid @RequestBody LoginRequest request){
        LoginResponse response = loginService.loginInDumy(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(response);
    }
}