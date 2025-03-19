package project.domain.schedule.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.domain.schedule.domain.DevLog;

@Getter
@Setter
@NoArgsConstructor
public class AddChatResponse {
    private Long scheduleId;
    private Long chatId;
    private String nickName;
    private String content;

    public static AddChatResponse from(DevLog chat) {
        AddChatResponse response = new AddChatResponse();

        response.setScheduleId(chat.getSchedule().getId());
        response.setChatId(chat.getId());
        response.setNickName(chat.getUser().getNickName());
        response.setContent(chat.getContent());

        return response;
    }
}
