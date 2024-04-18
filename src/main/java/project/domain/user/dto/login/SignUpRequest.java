package project.domain.user.dto.login;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;

@Getter 
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest{
    @Email
    @NotBlank
    private String email;
    
    @NotBlank
    private String password;
}