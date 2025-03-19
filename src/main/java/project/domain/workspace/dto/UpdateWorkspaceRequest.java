package project.domain.workspace.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateWorkspaceRequest {

    private String name;

    private String profile;
}
