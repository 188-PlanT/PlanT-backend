package project.api;

import project.domain.*;
import project.dto.login.*;
import project.service.UserService;
import project.common.auth.jwt.JwtProvider;
import project.exception.auth.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;


@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController{
    
    private final UserService userService;
    private final JwtProvider jwtProvider;
    
    //String으로 토큰 반환하기
    @PostMapping("/v1/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest request){ 
        User loginUser = userService.signIn(request.getEmail(), request.getPassword());

        String accessToken = jwtProvider.createAccessToken(loginUser);
        
        String refreshToken = jwtProvider.createRefreshToken(loginUser);
        
        LoginResponse response = new LoginResponse(accessToken, refreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/v1/refresh")
    public ResponseEntity<AccessTokenResponse> refreshToken(@RequestHeader("Refresh-Token") String refreshToken){
        
        String accessToken = jwtProvider.createAccessToken(refreshToken);
        
        AccessTokenResponse response = new AccessTokenResponse(accessToken);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/noToken")
    public ResponseEntity<String> noTokenError(){
        throw new UnIdentifiedUserException("로그인이 필요한 요청입니다");    
    }
}