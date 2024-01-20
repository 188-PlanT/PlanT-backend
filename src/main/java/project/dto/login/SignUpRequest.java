package project.dto.login;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import project.domain.User;
import project.domain.UserRole;

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