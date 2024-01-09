package project.domain;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import static java.util.stream.Collectors.toList;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
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
    
    //연관관계 삭제용
    @OneToMany(mappedBy="schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSchedule> userSchedules = new ArrayList <> ();
        
    @OneToMany(mappedBy="schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DevLog> devLogs = new ArrayList <> ();
    
    
    //< == 생성자 ==>
    protected Schedule() {} // JPA용 생성자
    
    @Builder //빌더 패턴 사용
    public Schedule(Workspace workspace, String name, LocalDateTime startDate, LocalDateTime endDate, String content){
        this.workspace = workspace;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.content = content;
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
    
    //수정
    public void update(String name, List<User> users){
        this.name = name;
        this.userSchedules.clear();
        
        for (User user : users){
            this.addUser(user);
        }
    }
    
    private boolean hasUser(User user){
        return (this.userSchedules.stream()
                                .filter(us -> us.getUser().equals(user))
                                .count() == 1);
    }
}