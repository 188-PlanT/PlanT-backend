package project.domain.schedule.domain;

import static org.assertj.core.api.Assertions.*;
import static project.common.constant.ScheduleConstant.*;
import static project.common.constant.UrlConstant.*;
import static project.common.constant.UserConstant.*;
import static project.common.constant.WorkspaceConstant.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import project.domain.workspace.domain.Workspace;

class ScheduleTest {

    @Nested
    class 스케줄_생성시 {

        @Test
        void 초기값은_TODO이다() {
            // given
            Workspace workspace = Workspace.create(WORKSPACE_NAME);

            // when
            Schedule schedule =
                    Schedule.create(workspace, SCHEDULE_NAME, SCHEDULE_START_DATE, SCHEDULE_END_DATE, SCHEDULE_CONTENT);

            // then
            assertThat(schedule.getState()).isEqualTo(Progress.TODO);
        }
    }

    @Nested
    class 스케줄_수정시 {

        @Test
        void 성공한다() {
            // given
            Workspace workspace = Workspace.create(WORKSPACE_NAME);

            Schedule schedule =
                    Schedule.create(workspace, SCHEDULE_NAME, SCHEDULE_START_DATE, SCHEDULE_END_DATE, SCHEDULE_CONTENT);
            String updatedName = "Updated Schedule";
            Progress updatedState = Progress.INPROGRESS;

            // when & then
            assertThatCode(() -> schedule.update(
                            updatedName, SCHEDULE_START_DATE, SCHEDULE_END_DATE, SCHEDULE_CONTENT, updatedState))
                    .doesNotThrowAnyException();

            assertThat(schedule.getName()).isEqualTo(updatedName);
            assertThat(schedule.getState()).isEqualTo(updatedState);
        }
    }

    @Nested
    class 스케줄_상태_수정시 {

        @Test
        void 성공한다() {
            // given
            Workspace workspace = Workspace.create(WORKSPACE_NAME);

            Schedule schedule =
                    Schedule.create(workspace, SCHEDULE_NAME, SCHEDULE_START_DATE, SCHEDULE_END_DATE, SCHEDULE_CONTENT);
            Progress updatedState = Progress.INPROGRESS;

            // when
            schedule.moveProgress(updatedState);

            assertThat(schedule.getState()).isEqualTo(updatedState);
        }
    }
}
