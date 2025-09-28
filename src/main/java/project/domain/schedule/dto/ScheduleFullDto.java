package project.domain.schedule.dto;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import project.domain.chat.domain.Chat;
import project.domain.chat.dto.ChatDto;
import project.domain.schedule.domain.Progress;
import project.domain.schedule.domain.Schedule;
import project.domain.user.domain.User;

public record ScheduleFullDto(
        Long scheduleId,
        Long workspaceId,
        String workspaceName,
        String name,
        List<UserDto> users,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String content,
        Progress state,
        List<ChatDto> chatList) {
    public static ScheduleFullDto from(Schedule schedule, List<User> usersInSchedule, List<Chat> chats) {
        List<UserDto> userDtos = usersInSchedule.stream().map(UserDto::of).collect(toList());
        List<ChatDto> chatDtos = chats.stream().map(ChatDto::from).toList();

        return new ScheduleFullDto(
                schedule.getId(),
                schedule.getWorkspace().getId(),
                schedule.getWorkspace().getName(),
                schedule.getName(),
                userDtos,
                schedule.getStartDate(),
                schedule.getEndDate(),
                schedule.getContent(),
                schedule.getState(),
                chatDtos);
    }

    public record UserDto(Long userId, String nickName) {
        public static UserDto of(User user) {
            return new UserDto(user.getId(), user.getNickName());
        }
    }
}
