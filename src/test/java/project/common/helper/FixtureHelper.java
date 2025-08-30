package project.common.helper;

import static project.common.constant.ScheduleConstant.SCHEDULE_NAME;
import static project.common.constant.UrlConstant.*;
import static project.common.constant.UserConstant.*;
import static project.common.constant.WorkspaceConstant.WORKSPACE_NAME;

import project.domain.chat.domain.Chat;
import project.domain.image.domain.Image;
import project.domain.schedule.domain.Schedule;
import project.domain.user.domain.User;
import project.domain.workspace.domain.Workspace;

public class FixtureHelper {

    public static User createEmailUser(String email) {
        Image profile = createDefaultUserProfile();
        return User.ofEmailPassword(EMAIL, PASSWORD, profile);
    }

    public static Workspace createWorkspace(User creator) {
        Image profile = createDefaultWorkspaceProfile();
        return Workspace.builder()
                .name(WORKSPACE_NAME)
                .user(creator)
                .profile(profile)
                .build();
    }

    public static Schedule createSchedule(Workspace workspace) {
        return Schedule.builder().workspace(workspace).name(SCHEDULE_NAME).build();
    }

    public static Chat createChat(Schedule schedule, User user, String content) {
        return Chat.builder().schedule(schedule).user(user).content(content).build();
    }

    private static Image createDefaultUserProfile() {
        return new Image(DEFAULT_USER_PROFILE_URL);
    }

    private static Image createDefaultWorkspaceProfile() {
        return new Image(DEFAULT_WORKSPACE_PROFILE_URL);
    }
}
