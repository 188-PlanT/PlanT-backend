package project.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import project.domain.User;
import project.domain.UserRole;

import javax.validation.constraints.NotBlank;

@Getter 
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest{
    @NotBlank
    private String email;
    
    @NotBlank
    private String password;
}