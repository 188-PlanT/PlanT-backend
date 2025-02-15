package project.domain.user.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class NickNameCheckRequest {
    
    @NotBlank 
    private String nickName;
}