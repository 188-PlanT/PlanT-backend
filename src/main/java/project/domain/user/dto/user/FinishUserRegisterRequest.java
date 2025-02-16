package project.domain.user.dto.user;

import lombok.Getter;

import jakarta.validation.constraints.NotBlank;

@Getter
public class FinishUserRegisterRequest{

    @NotBlank
    private String nickName;
}