package project.domain.workspaceUser.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.common.helper.FixtureHelper;
import project.domain.user.domain.User;
import project.domain.workspace.domain.Workspace;

public class WorkspaceUserDomainServiceTest {

    WorkspaceUserDomainService workspaceUserDomainService = new WorkspaceUserDomainService();

    @Nested
    class 유저_권한_변경시 {

        @Test
        void 마지막_어드민을_유저로_변경하면_실패한다() {
            // given
            User user = FixtureHelper.createEmailUser("test1@gmail.com");
            Workspace workspace = FixtureHelper.createWorkspace();

            WorkspaceUser workspaceUser = WorkspaceUser.createAdmin(workspace, user);
            List<WorkspaceUser> workspaceUsers = List.of(workspaceUser);

            // when, then
            assertThatThrownBy(() -> workspaceUserDomainService.validateWhenChangeRole(
                            workspaceUser, WorkspaceUserRole.USER, workspaceUsers))
                    .isInstanceOf(PlantException.class)
                    .hasMessage(ErrorCode.WORKSPACE_ADMIN_NOT_EXIST.getMessage());
        }
    }

    @Nested
    class 유저_삭제시 {

        @Test
        void 마지막_어드민을_삭제하면_실패한다() {
            // given
            User user = FixtureHelper.createEmailUser("test1@gmail.com");
            Workspace workspace = FixtureHelper.createWorkspace();

            WorkspaceUser workspaceUser = WorkspaceUser.createAdmin(workspace, user);
            List<WorkspaceUser> workspaceUsers = List.of(workspaceUser);

            // when, then
            assertThatThrownBy(() -> workspaceUserDomainService.validateWhenRemoveUser(workspaceUser, workspaceUsers))
                    .isInstanceOf(PlantException.class)
                    .hasMessage(ErrorCode.WORKSPACE_ADMIN_NOT_EXIST.getMessage());
        }
    }
}
