package project.dto.user;

import lombok.Getter;

@Getter
public class CreateUserResponse {
    private Long userId;
    private String email;

    public CreateUserResponse(Long userId, String email){
        this.userId = userId;
        this.email = email;
    }
}
