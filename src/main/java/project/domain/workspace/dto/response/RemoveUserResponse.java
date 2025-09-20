package project.domain.workspace.dto.response;

public record RemoveUserResponse(String message) {
    public RemoveUserResponse() {
        this("successfully delete user");
    }
}
