package project.domain;


import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "user_schedule")
@Getter @Setter
public class UserSchedule extends BaseEntity{
    
    @Id @GeneratedValue
    @Column(name = "user_schedule_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    // < == 생성자 ==>
    protected UserSchedule() {} //JPA용 생성자
    
    @Builder // 빌더 패턴 사용
    public UserSchedule(User user, Schedule schedule){
        this.user = user;
        this.schedule = schedule;
    }
}