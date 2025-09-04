package project.domain.chat.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.chat.domain.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByScheduleId(Long scheduleId);
}
