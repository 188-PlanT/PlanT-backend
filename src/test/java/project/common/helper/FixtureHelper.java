package project.common.helper;

import static project.common.constant.ImageConstant.PROFILE_URL;
import static project.common.constant.ScheduleConstant.*;
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
        return User.ofEmailPassword(email, PASSWORD, profile);
    }

    public static Workspace createWorkspace() {
        return Workspace.create(WORKSPACE_NAME);
    }

    public static Schedule createSchedule(Workspace workspace) {
        return Schedule.create(workspace, SCHEDULE_NAME, SCHEDULE_START_DATE, SCHEDULE_END_DATE, SCHEDULE_CONTENT);
    }

    public static Chat createChat(Schedule schedule, User user, String content) {
        return Chat.builder().schedule(schedule).user(user).content(content).build();
    }

    public static Image createImage() {
        return new Image(PROFILE_URL);
    }

    private static Image createDefaultUserProfile() {
        return new Image(DEFAULT_USER_PROFILE_URL);
    }

    private static Image createDefaultWorkspaceProfile() {
        return new Image(DEFAULT_WORKSPACE_PROFILE_URL);
    }
}
