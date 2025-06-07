package project.domain.schedule.domain;

import static org.assertj.core.api.Assertions.*;
import static project.common.constant.ScheduleConstant.*;
import static project.common.constant.UrlConstant.*;
import static project.common.constant.UserConstant.*;
import static project.common.constant.WorkspaceConstant.*;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.domain.image.domain.Image;
import project.domain.user.domain.User;
import project.domain.workspace.domain.Workspace;

class ScheduleTest {

    @Nested
    class 스케줄_생성시 {

        @Test
        void 성공한다() {
            // given
            Image defaultUserImage = new Image(DEFAULT_USER_PROFILE_URL);

            User user1 = User.fromOAuth2Attributes(EMAIL + 1, defaultUserImage);
            User user2 = User.fromOAuth2Attributes(EMAIL + 2, defaultUserImage);

            Workspace workspace =
                    Workspace.builder().name(WORKSPACE_NAME).user(user1).build();
            workspace.addUser(user2);

            // then & then
            assertThatCode(() -> Schedule.builder()
                            .workspace(workspace)
                            .name(SCHEDULE_NAME)
                            .startDate(SCHEDULE_START_DATE)
                            .endDate(SCHEDULE_END_DATE)
                            .content(SCHEDULE_CONTENT)
                            .users(List.of(user1, user2))
                            .state(Progress.TODO)
                            .build())
                    .doesNotThrowAnyException();
        }

        @Test
        void 워크스페이스에_없는_유저를_추가하면_실패한다() {
            // given
            Image defaultUserImage = new Image(DEFAULT_USER_PROFILE_URL);

            User user1 = User.fromOAuth2Attributes(EMAIL + 1, defaultUserImage);
            User invalidUser = User.fromOAuth2Attributes(EMAIL + 2, defaultUserImage);

            Workspace workspace =
                    Workspace.builder().name(WORKSPACE_NAME).user(user1).build();

            // then & then
            assertThatThrownBy(() -> Schedule.builder()
                            .workspace(workspace)
                            .name(SCHEDULE_NAME)
                            .startDate(SCHEDULE_START_DATE)
                            .endDate(SCHEDULE_END_DATE)
                            .content(SCHEDULE_CONTENT)
                            .users(List.of(user1, invalidUser))
                            .state(Progress.TODO)
                            .build())
                    .isInstanceOf(PlantException.class)
                    .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
        }

        @Test
        void 유저를_중복으로_추가하면_실패한다() {
            // given
            Image defaultUserImage = new Image(DEFAULT_USER_PROFILE_URL);

            User user1 = User.fromOAuth2Attributes(EMAIL + 1, defaultUserImage);
            User user2 = User.fromOAuth2Attributes(EMAIL + 2, defaultUserImage);

            Workspace workspace =
                    Workspace.builder().name(WORKSPACE_NAME).user(user1).build();
            workspace.addUser(user2);

            // then & then
            assertThatThrownBy(() -> Schedule.builder()
                            .workspace(workspace)
                            .name(SCHEDULE_NAME)
                            .startDate(SCHEDULE_START_DATE)
                            .endDate(SCHEDULE_END_DATE)
                            .content(SCHEDULE_CONTENT)
                            .users(List.of(user1, user2, user2))
                            .state(Progress.TODO)
                            .build())
                    .isInstanceOf(PlantException.class)
                    .hasMessageContaining(ErrorCode.USER_ALREADY_EXIST.getMessage());
        }
    }

    @Nested
    class 스케줄_수정시 {

        @Test
        void 성공한다() {
            // given
            Image defaultUserImage = new Image(DEFAULT_USER_PROFILE_URL);

            User user1 = User.fromOAuth2Attributes(EMAIL + 1, defaultUserImage);
            User user2 = User.fromOAuth2Attributes(EMAIL + 2, defaultUserImage);

            Workspace workspace =
                    Workspace.builder().name(WORKSPACE_NAME).user(user1).build();
            workspace.addUser(user2);

            Schedule schedule = Schedule.builder()
                    .workspace(workspace)
                    .name(SCHEDULE_NAME)
                    .startDate(SCHEDULE_START_DATE)
                    .endDate(SCHEDULE_END_DATE)
                    .content(SCHEDULE_CONTENT)
                    .users(List.of(user1, user2))
                    .state(Progress.TODO)
                    .build();

            // when & then
            assertThatCode(() -> schedule.update(
                            "Updated Schedule",
                            SCHEDULE_START_DATE.plusHours(1),
                            SCHEDULE_END_DATE.plusHours(1),
                            "Updated Content",
                            List.of(user1),
                            Progress.INPROGRESS))
                    .doesNotThrowAnyException();

            assertThat(schedule.getName()).isNotEqualTo(SCHEDULE_NAME);
        }

        @Test
        void 워크스페이스에_없는_유저를_추가하면_실패한다() {
            // given
            Image defaultUserImage = new Image(DEFAULT_USER_PROFILE_URL);

            User user1 = User.fromOAuth2Attributes(EMAIL + 1, defaultUserImage);
            User invalidUser = User.fromOAuth2Attributes(EMAIL + 2, defaultUserImage);

            Workspace workspace =
                    Workspace.builder().name(WORKSPACE_NAME).user(user1).build();

            Schedule schedule = Schedule.builder()
                    .workspace(workspace)
                    .name(SCHEDULE_NAME)
                    .startDate(SCHEDULE_START_DATE)
                    .endDate(SCHEDULE_END_DATE)
                    .content(SCHEDULE_CONTENT)
                    .users(List.of(user1))
                    .state(Progress.TODO)
                    .build();

            // when & then
            assertThatThrownBy(() -> schedule.update(
                            "Updated Schedule",
                            SCHEDULE_START_DATE.plusHours(1),
                            SCHEDULE_END_DATE.plusHours(1),
                            "Updated Content",
                            List.of(invalidUser),
                            Progress.INPROGRESS))
                    .isInstanceOf(PlantException.class)
                    .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
        }

        @Test
        void 유저를_중복으로_추가하면_실패한다() {
            // given
            Image defaultUserImage = new Image(DEFAULT_USER_PROFILE_URL);

            User user1 = User.fromOAuth2Attributes(EMAIL + 1, defaultUserImage);
            User user2 = User.fromOAuth2Attributes(EMAIL + 2, defaultUserImage);

            Workspace workspace =
                    Workspace.builder().name(WORKSPACE_NAME).user(user1).build();
            workspace.addUser(user2);

            Schedule schedule = Schedule.builder()
                    .workspace(workspace)
                    .name(SCHEDULE_NAME)
                    .startDate(SCHEDULE_START_DATE)
                    .endDate(SCHEDULE_END_DATE)
                    .content(SCHEDULE_CONTENT)
                    .users(List.of(user1, user2))
                    .state(Progress.TODO)
                    .build();

            // when & then
            assertThatThrownBy(() -> schedule.update(
                            "Updated Schedule",
                            SCHEDULE_START_DATE.plusHours(1),
                            SCHEDULE_END_DATE.plusHours(1),
                            "Updated Content",
                            List.of(user1, user2, user2),
                            Progress.INPROGRESS))
                    .isInstanceOf(PlantException.class)
                    .hasMessageContaining(ErrorCode.USER_ALREADY_EXIST.getMessage());
        }
    }
}
