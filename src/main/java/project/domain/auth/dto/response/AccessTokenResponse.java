package project.domain.auth.dto.response;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class AccessTokenResponse{
    private String accessToken;
}