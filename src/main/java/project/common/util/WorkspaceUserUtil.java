package project.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.domain.workspaceUser.dao.WorkspaceUserRepository;
import project.domain.workspaceUser.domain.WorkspaceUser;

/**
 * 워크스페이스 사용자 권한 조회를 위한 유틸리티 클래스입니다.
 * TODO: AOP 기반 구현 변경 검토
 */
@Component
@RequiredArgsConstructor
public class WorkspaceUserUtil {

    private final WorkspaceUserRepository workspaceUserRepository;

    public boolean isAdminUser(Long workspaceId, Long userId) {
        // unique key 인덱스를 활용하기 위해 조회 후 권한 검증
        WorkspaceUser workspaceUser = workspaceUserRepository.findByWorkspaceIdAndUserId(workspaceId, userId)
                .orElseThrow(() -> new PlantException(ErrorCode.WORKSPACE_USER_NOT_FOUND));

        return workspaceUser.getRole().isAdmin();
    }

    public boolean existsUser(Long workspaceId, Long userId) {
        return workspaceUserRepository.existsByWorkspaceIdAndUserId(workspaceId, userId);
    }
}
