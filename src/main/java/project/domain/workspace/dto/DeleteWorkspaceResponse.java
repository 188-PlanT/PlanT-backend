package project.domain.workspace.dto;

import lombok.Getter;

@Getter
public class DeleteWorkspaceResponse {
    private String message;

    public DeleteWorkspaceResponse() {
        this.message = "successfully delete workspace";
    }
}
