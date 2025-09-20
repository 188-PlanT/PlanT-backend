package project.domain.chat.dto;

import java.time.LocalDateTime;
import project.domain.chat.domain.Chat;

public record ChatDto(Long chatId, Long userId, String nickName, String content, LocalDateTime createDate) {

    public static ChatDto from(Chat chat) {
        return new ChatDto(
                chat.getId(),
                chat.getUser().getId(),
                chat.getUser().getNickName(),
                chat.getContent(),
                chat.getCreateDate());
    }
}
