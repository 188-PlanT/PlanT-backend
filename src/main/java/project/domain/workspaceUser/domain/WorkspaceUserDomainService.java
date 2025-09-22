package project.domain.workspaceUser.domain;

import java.util.List;
import org.springframework.stereotype.Component;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;

@Component
public class WorkspaceUserDomainService {

    /**
     * 워크스페이스의 마지막 관리자를 일반 사용자로 변경하는 것을 방지합니다.
     */
    public void validateWhenChangeRole(
            WorkspaceUser workspaceUser, WorkspaceUserRole newRole, List<WorkspaceUser> currentUsers) {
        boolean isAdminChangingToUser = workspaceUser.getRole().isAdmin() && newRole.isUser();
        long adminCount =
                currentUsers.stream().filter(user -> user.getRole().isAdmin()).count();

        if (isAdminChangingToUser && adminCount <= 1) {
            throw new PlantException(ErrorCode.WORKSPACE_ADMIN_NOT_EXIST);
        }
    }

    /**
     * 워크스페이스의 마지막 관리자를 삭제하는 것을 방지합니다.
     */
    public void validateWhenRemoveUser(WorkspaceUser workspaceUser, List<WorkspaceUser> currentUsers) {
        boolean isAdminBeingRemoved = workspaceUser.getRole().isAdmin();
        long adminCount =
                currentUsers.stream().filter(user -> user.getRole().isAdmin()).count();

        if (isAdminBeingRemoved && adminCount <= 1) {
            throw new PlantException(ErrorCode.WORKSPACE_ADMIN_NOT_EXIST);
        }
    }
}
