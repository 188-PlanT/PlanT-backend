package project.domain.workspace.dto;

import lombok.Getter;

@Getter
public class RemoveUserResponse {
    private String message;

    public RemoveUserResponse() {
        this.message = "successfully delete user";
    }
}
