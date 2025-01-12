package project.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Oauth2LoginRequest{
            @NotBlank private String code;
            @NotBlank private String provider;
};