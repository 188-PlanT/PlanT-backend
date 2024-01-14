package project.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import project.domain.User;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest{
    @NotBlank
    private String password;
    
    @NotBlank
    private String name;
}