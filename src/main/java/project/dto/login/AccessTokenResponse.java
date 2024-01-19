package project.dto.login;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class AccessTokenResponse{
    private String accessToken;
}