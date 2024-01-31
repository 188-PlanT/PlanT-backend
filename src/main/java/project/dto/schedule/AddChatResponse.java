package project.dto.schedule;

import project.domain.DevLog;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter @Setter
@NoArgsConstructor
public class AddChatResponse {
    private Long scheduleId;
    private Long chatId;
    private String nickName;
    private String content;
    
    public static AddChatResponse from(DevLog chat){
        AddChatResponse response = new AddChatResponse();
        
        response.setScheduleId(chat.getSchedule().getId());
        response.setChatId(chat.getId());
        response.setNickName(chat.getUser().getNickName());
        response.setContent(chat.getContent());
        
        return response;
    }
}