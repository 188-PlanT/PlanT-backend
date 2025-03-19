package project.domain.user.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FinishUserRegisterRequest {

    @NotBlank private String nickName;
}
