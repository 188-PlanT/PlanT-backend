package project.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_schedule")
@Getter @Setter
@NoArgsConstructor
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
    
    public UserSchedule(User user, Schedule schedule){
        this.user = user;
        this.schedule = schedule;
    }
}