package project.domain.user.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest{
    @NotBlank
    private String nickName;
    
    @NotBlank
    private String password;
    
    private String profile;
}