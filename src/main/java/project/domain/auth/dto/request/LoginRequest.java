package project.domain.auth.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Getter 
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest{
    
    @Email
    @NotBlank
    private String email;
    
    @NotBlank
    private String password;
}