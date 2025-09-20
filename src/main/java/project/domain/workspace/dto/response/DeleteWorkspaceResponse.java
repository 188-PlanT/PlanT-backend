package project.domain.workspace.dto.response;

public record DeleteWorkspaceResponse(String message) {
    public DeleteWorkspaceResponse() {
        this("successfully delete workspace");
    }
}
