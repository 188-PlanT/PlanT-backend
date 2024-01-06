package project.domain;

import javax.persistence.*;
import lombok.Getter;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

@Entity
@Table(name = "dev_logs")
@Getter
public class DevLog extends BaseEntity{
    
    @Id @GeneratedValue
    @Column(name = "dev_log_id")
    private Long id;
    
    private String content;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    protected DevLog(){}
    
    public DevLog(Schedule schedule, User user, String content){
        this.schedule = schedule;
        this.user = user;
        this.content = content;
    }
    
    // < == 수정 로직 ==>
    public void updateContent(String content){
        this.content = content;
    }
}