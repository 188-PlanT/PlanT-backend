package project.domain.workspace.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public record WorkspaceCreateRequest(@NotBlank String name, String profile, List<Long> users) {

    public WorkspaceCreateRequest {
        if (users == null) {
            users = new ArrayList<>();
        }
    }
}
