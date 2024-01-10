package project.api;

import project.domain.*;
import project.dto.login.*;
import project.dto.ErrorResponse;
import project.service.UserService;
import project.exception.user.NoSuchUserException;
import project.exception.ValidateException;
import project.common.auth.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController{
    
    private final UserService userService;
    private final JwtProvider jwtProvider;
    
    // //String으로 토큰 반환하기
    // @PostMapping("/v1/login")
    // public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest request){ 
    //     User loginUser = userService.signIn(request.getEmail(), request.getPassword());

    //     String accessToken = jwtProvider.createAccessToken(loginUser);
        
    //     String refreshToken = jwtProvider.createRefreshToken(loginUser);
        
    //     LoginResponse response = new LoginResponse(accessToken, refreshToken);

    //     return ResponseEntity.ok(response);
    // }

    // @PostMapping("/v1/refresh")
    // public ResponseEntity<String> refreshToken(@RequestHeader("Refresh-Token") String refreshToken){
        
    //     if(jwtProvider.validateRefreshToken(refreshToken)){
            
    //         String email = getUserEmail(refreshToken);
            
    //         User user = userService.findByEmail(email);
            
    //         String accessToken = jwtProvider.createAccessToken(user);
            
    //         return ResponseEntity.ok(accessToken);
    //     }
    //     return ResponseEntity.ok("Unvalid Refresh-Token");
    // }
    
    // private String getUserEmail(String refreshToken){
    //     refreshToken = refreshToken.replace("Bearer ", "");
        
    //     return jwtProvider.parseClaims(refreshToken)
    //             .get("email")
    //             .toString();
    // }
}