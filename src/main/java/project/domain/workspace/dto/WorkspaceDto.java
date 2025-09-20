package project.domain.workspace.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import project.domain.workspace.domain.Workspace;

public record WorkspaceDto(Long workspaceId, String name, String profile, List<Long> users) {
    public static WorkspaceDto from(Workspace workspace) {
        List<Long> userIds = workspace.getUserWorkspaces().stream()
                .map(uw -> uw.getUser().getId())
                .collect(toList());

        return new WorkspaceDto(
                workspace.getId(), workspace.getName(), workspace.getProfile().getUrl(), userIds);
    }
}
