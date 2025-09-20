package project.domain.workspace.dto.response;

import project.domain.workspace.domain.Workspace;

public record UpdateWorkspaceResponse(Long workspaceId, String name, String profile) {
    public static UpdateWorkspaceResponse from(Workspace workspace) {
        return new UpdateWorkspaceResponse(
                workspace.getId(), workspace.getName(), workspace.getProfile().getUrl());
    }
}
