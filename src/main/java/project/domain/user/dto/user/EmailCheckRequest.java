package project.domain.user.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
public class EmailCheckRequest {
    
    @NotBlank @Email
    private String email;
}
