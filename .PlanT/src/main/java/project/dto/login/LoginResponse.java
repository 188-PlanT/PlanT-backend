package project.dto.login;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import project.domain.User;

@Getter 
@NoArgsConstructor
public class LoginResponse{
    
    private String accessToken;
    private String refreshToken;
    
    public LoginResponse(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}