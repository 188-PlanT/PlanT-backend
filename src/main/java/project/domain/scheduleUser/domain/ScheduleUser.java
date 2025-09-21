package project.domain.scheduleUser.domain;

import jakarta.persistence.*;
import lombok.Getter;
import project.domain.common.BaseEntity;
import project.domain.schedule.domain.Schedule;
import project.domain.user.domain.User;

@Getter
@Entity
@Table(
        name = "schedule_user",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "schedule_user_unique",
                    columnNames = {"schedule_id", "user_id"})
        })
public class ScheduleUser extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "schedule_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    protected ScheduleUser() {} // JPA용 생성자

    private ScheduleUser(Schedule schedule, User user) { // 기본 생성자
        this.schedule = schedule;
        this.user = user;
    }

    public static ScheduleUser create(Schedule schedule, User user) {
        return new ScheduleUser(schedule, user);
    }
}
