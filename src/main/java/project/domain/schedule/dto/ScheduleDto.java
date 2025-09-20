package project.domain.schedule.dto;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import project.domain.chat.domain.Chat;
import project.domain.chat.dto.ChatDto;
import project.domain.schedule.domain.Progress;
import project.domain.schedule.domain.Schedule;
import project.domain.user.domain.User;

public record ScheduleDto(
        Long scheduleId,
        Long workspaceId,
        String workspaceName,
        String name,
        List<UserDto> users,
        @JsonFormat(pattern = "yyyyMMdd:HH:mm") LocalDateTime startDate,
        @JsonFormat(pattern = "yyyyMMdd:HH:mm") LocalDateTime endDate,
        String content,
        Progress state,
        List<ChatDto> chatList) {
    public static ScheduleDto from(Schedule schedule, List<Chat> chats) {
        List<UserDto> userDtos = schedule.getUserSchedules().stream()
                .map(us -> new UserDto(us.getUser()))
                .collect(toList());

        List<ChatDto> chatDtos = chats.stream().map(ChatDto::from).toList();

        return new ScheduleDto(
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
        public UserDto(User user) {
            this(user.getId(), user.getNickName());
        }
    }
}
