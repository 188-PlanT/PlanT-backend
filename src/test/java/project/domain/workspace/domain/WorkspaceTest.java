package project.domain.workspace.domain;

import static org.assertj.core.api.Assertions.*;
import static project.common.constant.UrlConstant.*;
import static project.common.constant.UserConstant.*;
import static project.common.constant.WorkspaceConstant.WORKSPACE_NAME;
import static project.domain.user.domain.UserRole.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import project.common.helper.FixtureHelper;
import project.domain.image.domain.Image;

class WorkspaceTest {

    @Nested
    class 워크스페이스_생성시 {

        @Test
        void 프로필_초기값은_NULL이다() {
            // given & when
            Workspace workspace = Workspace.create(WORKSPACE_NAME);

            // then
            assertThat(workspace.getProfile()).isNull();
        }
    }

    @Nested
    class 워크스페이스_수정시 {

        @Test
        void 성공한다() {
            // given
            Workspace workspace = FixtureHelper.createWorkspace();
            String updatedName = "new workspace name";
            Image updatedProfile = FixtureHelper.createImage();

            // when
            workspace.update(updatedName, updatedProfile);

            // then
            assertThat(workspace.getName()).isEqualTo(updatedName);
            assertThat(workspace.getProfile()).isEqualTo(updatedProfile);
        }
    }
}
