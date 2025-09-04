package project.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import project.domain.chat.domain.Chat;

public record ChatDto(
        Long chatId,
        Long userId,
        String nickName,
        String content,
        @JsonFormat(pattern = "yyyyMMdd:HH:mm:ss") LocalDateTime createDate) {
    public static ChatDto from(Chat chat) {
        return new ChatDto(
                chat.getId(),
                chat.getUser().getId(),
                chat.getUser().getNickName(),
                chat.getContent(),
                chat.getCreateDate());
    }
}
