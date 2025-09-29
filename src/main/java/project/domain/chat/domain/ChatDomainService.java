package project.domain.chat.domain;

import org.springframework.stereotype.Component;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.domain.user.domain.User;

@Component
public class ChatDomainService {

    public void validateWhenUpdateOrDelete(Chat chat, User user) {
        if (!chat.getUser().equals(user)) {
            throw new PlantException(ErrorCode.CHAT_NOT_WRITER);
        }
    }
}
