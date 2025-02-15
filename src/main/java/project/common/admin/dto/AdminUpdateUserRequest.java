package project.common.admin.dto;

import project.domain.user.domain.User;
import project.domain.user.domain.UserRole;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@NoArgsConstructor
@Getter @Setter
public class AdminUpdateUserRequest {
    @NotBlank
    private String password;

    @NotBlank
    private String url;

    @NotNull
    private UserRole userRole;

    public AdminUpdateUserRequest(User user){
        this.url = user.getProfile().getUrl();
        this.userRole = user.getUserRole();
    }
}
