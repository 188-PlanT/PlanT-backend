package project.domain.user.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.domain.user.domain.User;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRequest {
    @NotBlank private String currentPassword;

    private String newPassword;

    private String nickName;

    private String profile;

    public UpdateUserRequest(User user) {
        this.nickName = user.getNickName();
        this.profile = user.getProfile().getUrl();
    }
}
