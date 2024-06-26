package project.domain.schedule.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.domain.BaseEntity;
import project.domain.user.domain.User;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;

@Entity
@Table(name = "dev_logs")
@Getter
@Setter
public class DevLog extends BaseEntity {
    
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
    
    // <== 생성자 ==>
    protected DevLog(){} //JPA용 생성자
    
    //@Builder //빌더 패턴 사용
    private DevLog(Builder builder){
        this.schedule = builder.schedule;
        this.user = builder.user;
        this.content = builder.content;
    }
    
    // < == 수정 로직 ==>
    public void updateContent(String content){
        this.content = content;
    }
    
    
    // <=== Builder 구현 ===>
    public static Builder builder(){
        return new Builder();
    }
    
    public static class Builder {
        private Schedule schedule;
        private User user;
        private String content;
        
        public Builder schedule(Schedule schedule){
            this.schedule = schedule;
            return this;
        }
        
        public Builder user(User user){
            this.user = user;
            return this;
        }
        
        public Builder content(String content){
            this.content = content;
            return this;
        }
        
        public DevLog build(){
            if (!schedule.hasUser(user)){
                throw new PlantException(ErrorCode.USER_NOT_FOUND, "스케줄에 존재하지 않는 유저입니다");
            }
            
            return new DevLog(this); 
        }
    }
}