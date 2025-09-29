package project.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.common.util.UserUtil;
import project.domain.chat.dao.ChatRepository;
import project.domain.chat.domain.Chat;
import project.domain.chat.domain.ChatDomainService;
import project.domain.chat.dto.request.ChatCreateRequest;
import project.domain.chat.dto.request.ChatUpdateRequest;
import project.domain.schedule.dao.ScheduleRepository;
import project.domain.schedule.domain.Schedule;
import project.domain.user.domain.User;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ScheduleRepository scheduleRepository;
    private final ChatDomainService chatDomainService;
    private final UserUtil userUtil;

    @Transactional
    public void createChat(ChatCreateRequest request) {
        User currentUser = userUtil.getLoginUser();
        Schedule schedule = scheduleRepository
                .findById(request.scheduleId())
                .orElseThrow(() -> new PlantException(ErrorCode.SCHEDULE_NOT_FOUND));

        Chat chat = Chat.builder()
                .content(request.content())
                .schedule(schedule)
                .user(currentUser)
                .build();
        chatRepository.save(chat);
    }

    @Transactional
    public void updateChat(Long chatId, ChatUpdateRequest request) {
        User currentUser = userUtil.getLoginUser();
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new PlantException(ErrorCode.CHAT_NOT_FOUND));
        chatDomainService.validateWhenUpdateOrDelete(chat, currentUser);

        chat.update(request.content());
        chatRepository.save(chat);
    }

    @Transactional
    public void deleteChat(Long chatId) {
        User currentUser = userUtil.getLoginUser();
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new PlantException(ErrorCode.CHAT_NOT_FOUND));
        chatDomainService.validateWhenUpdateOrDelete(chat, currentUser);

        chatRepository.delete(chat);
    }
}
