package project.domain.workspace.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateWorkspaceRequest {
    @NotBlank private String name;

    private String profile;

    private List<Long> users = new ArrayList<>();
}
