package project.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FinishUserRegisterRequest {

    @NotBlank private String nickName;
}
