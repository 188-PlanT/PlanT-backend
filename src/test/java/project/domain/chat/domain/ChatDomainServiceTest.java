package project.domain.chat.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.common.helper.FixtureHelper;
import project.domain.schedule.domain.Schedule;
import project.domain.user.domain.User;
import project.domain.workspace.domain.Workspace;

public class ChatDomainServiceTest {

    ChatDomainService chatDomainService = new ChatDomainService();

    @Nested
    class 생성_전_검증시 {

        @Test
        void 스케줄에_소속되지_않은_유저인_경우_실패한다() {
            // given
            User user = FixtureHelper.createEmailUser("test1@gmail.com");
            Workspace workspace = FixtureHelper.createWorkspace(user);
            Schedule schedule = FixtureHelper.createSchedule(workspace);

            User unAssignedUser = FixtureHelper.createEmailUser("unAssigned@gmail.com");

            // when & then
            assertThatThrownBy(() -> chatDomainService.validateWhenCreate(schedule, unAssignedUser))
                    .isInstanceOf(PlantException.class)
                    .hasMessage(ErrorCode.CHAT_USER_NOT_IN_SCHEDULE.getMessage());
        }
    }

    @Nested
    class 수정_혹은_삭제_전_검증시 {

        @Test
        void 채팅_생성자가_아니면_실패한다() {
            // given
            User user = FixtureHelper.createEmailUser("test1@gmail.com");
            Workspace workspace = FixtureHelper.createWorkspace(user);
            Schedule schedule = FixtureHelper.createSchedule(workspace);
            Chat chat = FixtureHelper.createChat(schedule, user, "Hello");

            User invalidUser = FixtureHelper.createEmailUser("invalid@gmail.com");

            // when & then
            assertThatThrownBy(() -> chatDomainService.validateWhenUpdateOrDelete(chat, invalidUser))
                    .isInstanceOf(PlantException.class)
                    .hasMessage(ErrorCode.CHAT_NOT_WRITER.getMessage());
        }
    }
}
