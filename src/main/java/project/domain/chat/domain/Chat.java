package project.domain.chat.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import project.domain.BaseEntity;
import project.domain.schedule.domain.Schedule;
import project.domain.user.domain.User;

@Entity
@Table(name = "chats")
@Getter
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "chat_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // <== 생성자 ==>
    protected Chat() {} // JPA용 생성자

    @Builder
    public Chat(Schedule schedule, User user, String content) {
        this.schedule = schedule;
        this.user = user;
        this.content = content;
    }

    // < == 수정 로직 ==>
    public void update(String content) {
        this.content = content;
    }
}
