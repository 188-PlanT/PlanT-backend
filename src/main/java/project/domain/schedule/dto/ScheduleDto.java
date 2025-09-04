package project.domain.schedule.dto;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.domain.chat.domain.Chat;
import project.domain.chat.dto.ChatDto;
import project.domain.schedule.domain.Progress;
import project.domain.schedule.domain.Schedule;
import project.domain.user.domain.User;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleDto {
    private Long scheduleId;
    private Long workspaceId;
    private String workspaceName;
    private String name;
    private List<UserDto> users = new ArrayList<>();

    @JsonFormat(pattern = "yyyyMMdd:HH:mm")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyyMMdd:HH:mm")
    private LocalDateTime endDate;

    private String content;
    private Progress state;
    private List<ChatDto> chatList = new ArrayList<>();

    public static ScheduleDto from(Schedule schedule, List<Chat> chats) {
        ScheduleDto dto = new ScheduleDto();

        dto.setScheduleId(schedule.getId());
        dto.setWorkspaceId(schedule.getWorkspace().getId());
        dto.setWorkspaceName(schedule.getWorkspace().getName());
        dto.setName(schedule.getName());
        dto.setStartDate(schedule.getStartDate());
        dto.setEndDate(schedule.getEndDate());
        dto.setContent(schedule.getContent());
        dto.setState(schedule.getState());

        dto.setUsers(schedule.getUserSchedules().stream()
                .map(us -> new UserDto(us.getUser()))
                .collect(toList()));

        dto.setChatList(chats.stream().map(ChatDto::from).toList());

        return dto;
    }

    @Getter
    public static class UserDto {
        private Long userId;
        private String nickName;

        public UserDto(User user) {
            this.userId = user.getId();
            this.nickName = user.getNickName();
        }
    }
}
