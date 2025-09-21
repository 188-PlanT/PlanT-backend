package project.domain.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.common.security.jwt.JwtProvider;
import project.domain.auth.dto.request.EmailLoginRequest;
import project.domain.auth.dto.request.Oauth2LoginRequest;
import project.domain.auth.dto.response.AccessTokenResponse;
import project.domain.auth.dto.response.TokenPairResponse;
import project.domain.auth.service.CustomOAuth2UserService;
import project.domain.auth.service.LoginService;

@Tag(name = "1. [Login]", description = "로그인, 로그아웃 API")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtProvider jwtProvider;
    private final LoginService loginService;

    @Operation(summary = "이메일 로그인", description = "이메일과 비밀번호 기반으로 로그인합니다. 토큰을 응답 본문에 반환합니다.")
    @PostMapping("/v1/login")
    public ResponseEntity<TokenPairResponse> loginUserByEmailAndPassword(
            @Valid @RequestBody EmailLoginRequest request) {
        var response = loginService.loginByEmailAndPassword(request.email(), request.password());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Oauth2 로그인",
            description = "Oauth2 기반으로 로그인합니다. Authentication code와 provider 정보를 입력하면, 사용자 정보 확인 후 토큰을 응답 본문에 반환합니다.")
    @PostMapping("/v1/login/oauth2")
    public ResponseEntity<TokenPairResponse> oauth2Login(@Valid @RequestBody Oauth2LoginRequest request) {
        // TODO: OAuth2 리팩토링 시 서비스로 이동
        String loginUserEmail =
                customOAuth2UserService.getOauth2UserEmailByAuthCode(request.code(), request.provider());
        var response = loginService.loginByOauth2UserEmail(loginUserEmail);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "액세스 토큰 재발급", description = "리프레시 토큰을 기반으로 액세스 토큰을 재발급합니다.")
    @PostMapping("/v1/refresh")
    public ResponseEntity<AccessTokenResponse> loginByRefreshToken(
            @RequestHeader("Refresh-Token") String refreshToken) {
        var response = jwtProvider.createAccessTokenByRefreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "더비 데이터 로그인", description = "더미 데이터 DB에 로그인합니다. 토큰을 응답 본문에 반환합니다.")
    @PostMapping("/v1/login/dumy")
    public ResponseEntity<TokenPairResponse> dumyLogin(@Valid @RequestBody EmailLoginRequest request) {
        var response = loginService.loginInDumy(request.email(), request.password());
        return ResponseEntity.ok(response);
    }
}
