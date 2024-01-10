package project.domain;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import static java.util.stream.Collectors.toList;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
// import lombok.Builder;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "schedules")
@Getter
public class Schedule extends BaseEntity{
    
    @Id @GeneratedValue
    @Column(name = "schedule_id")
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private LocalDateTime startDate;
    
    @Column(nullable = false)
    private LocalDateTime endDate;
    
    @Column(nullable = true)
    private String content;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;
    
    //Schedule이 UserSchedule 영속성 관리
    @OneToMany(mappedBy="schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSchedule> userSchedules = new ArrayList <> ();
    
    //연관관계 삭제용
    @OneToMany(mappedBy="schedule", orphanRemoval = true)
    private List<DevLog> devLogs = new ArrayList <> ();
    
    
    //< == 생성자 ==>
    protected Schedule() {} // JPA용 생성자
    
    
    //@Builder //빌더 패턴 사용
    private Schedule(Builder builder){
        this.workspace = builder.workspace;
        this.name = builder.name;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.content = builder.content;
    }
    
    // < == 비즈니스 로직 == >
    public void addUser(User user){
        if(!user.workspace.hasUser(user)){
            throw new NoSuchUserException("Workspace에 존재하지 않는 유저입니다");
        }
        
        if (this.hasUser(user)){
            throw new IllegalStateException("이미 존재하는 user 입니다");
        }
        
        UserSchedule userSchedule = new UserSchedule(user,this);
        
        this.userSchedules.add(userSchedule);
    }
    
    public void removeUser(User user){
        if (!this.hasUser(user)){
            throw new IllegalStateException("존재하지 않는 user 입니다");
        }
        
        this.userSchedules.removeIf(us -> us.getUser().equals(user));
    }
    
    public List<String> getUserEmailList(){
        List<String> emailList = this.getUserSchedules().stream()
            .map(us -> us.getUser().getEmail())
            .collect(toList());
        
        return emailList;
    }
    
    //수정 로직 -> 이거 DTO로 묶는 방법 생각해보자
    public void update(String name, LocalDateTime startDate, LocalDateTime endDate, String content, User<List> userList){
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.content = content;
        this.userSchedules.clear();
        
        for (User user : userList){
            this.addUser(user);
        }
    }
    
    public boolean hasUser(User user){
        return this.userSchedules.contains(user);
    }
    
    
    // <=== Builder ===>
    public static class Builder{
        private Workspace workspace;
        private String name;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String content;
        private List<User> users;
        
        public Builder builder(){
            return new Builder();
        }
        
        public Builder workspace(Workspace workspace){
            this.workspace = workspace;
            return this;
        }
        
        public Builder name(String name){
            this.name = name;
            return this;
        }
        
        public Builder startDate(LocalDateTime startDate){
            this.startDate = startDate;
            return this;
        }
        
        public Builder startDate(LocalDateTime endDate){
            this.end = endDate;
            return this;
        }
        
        public Builder content(String content){
            this.content = content;
            return this;
        }
        
        public Schedule build(){
            Schedule schedule = new Schedule(this);
                    
            for (User user : users){
                schedule.addUser(user);
            }
            
            return schedule;
        }
    }
}