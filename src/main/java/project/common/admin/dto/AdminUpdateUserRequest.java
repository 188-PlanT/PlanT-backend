package project.common.admin.dto;

import project.domain.user.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter @Setter
public class AdminUpdateUserRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String url;

    public AdminUpdateUserRequest(User user){
        this.email = user.getEmail();
        this.url = user.getProfile().getUrl();
    }
}
