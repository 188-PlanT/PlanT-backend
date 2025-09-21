package project.domain.workspaceUser.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkspaceUserRole {
    ADMIN("ADMIN", "어드민"),
    USER("USER", "사용자"),
    ;

    private final String key;
    private final String title;
}
