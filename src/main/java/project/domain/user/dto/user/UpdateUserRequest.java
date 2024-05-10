package project.domain.user.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
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
}