package project.domain.workspace.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.domain.workspace.domain.Workspace;

@Getter
@Setter
@NoArgsConstructor
public class UpdateWorkspaceResponse {

    private Long workspaceId;

    private String name;

    private String profile;

    public static UpdateWorkspaceResponse from(Workspace workspace) {
        UpdateWorkspaceResponse response = new UpdateWorkspaceResponse();

        response.setWorkspaceId(workspace.getId());
        response.setName(workspace.getName());
        response.setProfile(workspace.getProfile().getUrl());

        return response;
    }
}
