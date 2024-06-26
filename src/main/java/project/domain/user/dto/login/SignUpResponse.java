package project.domain.user.dto.login;

import lombok.Getter;

@Getter
public class SignUpResponse {
    private Long userId;
    private String email;

    public SignUpResponse(Long userId, String email){
        this.userId = userId;
        this.email = email;
    }
}
