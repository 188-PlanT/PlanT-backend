package project.domain.user.dto.user;

import project.domain.user.domain.User;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor
public class UpdateUserRequest{
    @NotBlank
    private String currentPassword;
	
	private String newPassword;
	
	private String nickName;
    
    private String profile;

    public UpdateUserRequest(User user){
        this.nickName = user.getNickName();
        this.profile = user.getProfile().getUrl();
    }
}