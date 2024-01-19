package project.dto.login;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter 
@AllArgsConstructor
public class LoginResponse{
    
    private String accessToken;
    private String refreshToken;
    
}