package project.domain.workspace.domain;

import static org.assertj.core.api.Assertions.*;
import static project.common.constant.UrlConstant.*;
import static project.common.constant.UserConstant.*;
import static project.common.constant.WorkspaceConstant.WORKSPACE_NAME;
import static project.domain.user.domain.UserRole.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.domain.image.domain.Image;
import project.domain.user.domain.User;

class WorkspaceTest {

    @Nested
    class 워크스페이스_생성시 {

        @Test
        void 생성_유저_권한은_ADMIN이다() {
            // given
            Image defaultUserImage = new Image(DEFAULT_USER_PROFILE_URL);
            User user = User.fromOAuth2Attributes(EMAIL, defaultUserImage);

            Image defaultWorkspaceImage = new Image(DEFAULT_WORKSPACE_PROFILE_URL);

            // when
            Workspace workspace = Workspace.builder()
                    .name(WORKSPACE_NAME)
                    .profile(defaultWorkspaceImage)
                    .user(user)
                    .build();

            // then
            UserWorkspace userWorkspace = workspace.getUserWorkspaces().get(0);
            assertThat(userWorkspace.getUserRole()).isEqualTo(ADMIN);
        }
    }

    @Nested
    class 워크스페이스_수정시 {

        @Test
        void 성공한다() {
            // given
            Image defaultUserImage = new Image(DEFAULT_USER_PROFILE_URL);
            User user = User.fromOAuth2Attributes(EMAIL, defaultUserImage);
            Image defaultWorkspaceImage = new Image(DEFAULT_WORKSPACE_PROFILE_URL);

            Workspace workspace = Workspace.builder()
                    .name(WORKSPACE_NAME)
                    .profile(defaultWorkspaceImage)
                    .user(user)
                    .build();

            // when
            workspace.updateWorkspace("new workspace name", defaultWorkspaceImage);

            // then
            assertThat(workspace.getName()).isNotEqualTo(WORKSPACE_NAME);
        }
    }

    @Nested
    class 유저_추가시 {

        @Test
        void 유저_권한은_일반_유저이다() {
            // given
            Image defaultUserImage = new Image(DEFAULT_USER_PROFILE_URL);
            User user1 = User.fromOAuth2Attributes(EMAIL + "1", defaultUserImage);
            Image defaultWorkspaceImage = new Image(DEFAULT_WORKSPACE_PROFILE_URL);

            Workspace workspace = Workspace.builder()
                    .name(WORKSPACE_NAME)
                    .profile(defaultWorkspaceImage)
                    .user(user1)
                    .build();

            User user2 = User.fromOAuth2Attributes(EMAIL + "2", defaultUserImage);

            // when
            workspace.addUser(user2);

            // then
            UserWorkspace userWorkspace = workspace.getUserWorkspaces().get(1);
            assertThat(userWorkspace.getUserRole()).isEqualTo(USER);
        }

        @Test
        void 이미_존재하는_유저를_추가하면_실패한다() {
            // given
            Image defaultUserImage = new Image(DEFAULT_USER_PROFILE_URL);
            User user1 = User.fromOAuth2Attributes(EMAIL + "1", defaultUserImage);

            Image defaultWorkspaceImage = new Image(DEFAULT_WORKSPACE_PROFILE_URL);
            Workspace workspace = Workspace.builder()
                    .name(WORKSPACE_NAME)
                    .profile(defaultWorkspaceImage)
                    .user(user1)
                    .build();

            User user2 = User.fromOAuth2Attributes(EMAIL + "1", defaultUserImage);
            workspace.addUser(user2);

            // when & then
            assertThatThrownBy(() -> workspace.addUser(user2))
                    .isInstanceOf(PlantException.class)
                    .hasMessage(ErrorCode.USER_ALREADY_EXIST.getMessage());
        }
    }

    @Nested
    class 유저_추방시 {

        void 어드민이_1명도_남지_않으면_실패한다() {
            // given
            Image defaultUserImage = new Image(DEFAULT_USER_PROFILE_URL);
            Image defaultWorkspaceImage = new Image(DEFAULT_WORKSPACE_PROFILE_URL);
            User user1 = User.fromOAuth2Attributes(EMAIL + "1", defaultUserImage);
            Workspace workspace = Workspace.builder()
                    .name(WORKSPACE_NAME)
                    .profile(defaultWorkspaceImage)
                    .user(user1)
                    .build();

            User user2 = User.fromOAuth2Attributes(EMAIL + "2", defaultUserImage);
            workspace.addUser(user2);

            // when & then
            assertThatThrownBy(() -> workspace.removeUser(user1))
                    .isInstanceOf(PlantException.class)
                    .hasMessage(ErrorCode.WORKSPACE_ADMIN_NOT_EXIST.getMessage());
        }
    }
}
