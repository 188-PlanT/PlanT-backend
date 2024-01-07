package project.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import static java.util.stream.Collectors.toList;

@Entity
@Table(name = "schedules")
@Getter @Setter
@NoArgsConstructor
public class Schedule extends BaseEntity{
    
    @Id @GeneratedValue
    @Column(name = "schedule_id")
    private Long id;
    
    private String name;
    
    private LocalDateTime startDate;
    
    private LocalDateTime endDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;
    
    @OneToMany(mappedBy="schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSchedule> userSchedules = new ArrayList <> ();
        
    @OneToMany(mappedBy="schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DevLog> devLogs = new ArrayList <> ();
    
    
    // 생성자
    public Schedule(Workspace workspace, String name, List<User> users){
        this.workspace = workspace;
        this.name = name;
        
        for (User user : users){
            this.addUser(user);
        }
    }
    
    
    // < == 비즈니스 로직 == >
    public void addUser(User user){
        if (this.userSchedules.stream().filter(us -> us.getUser().equals(user)).count() != 0){
            throw new IllegalStateException("이미 존재하는 user 입니다");
        }
        
        UserSchedule userSchedule = new UserSchedule(user,this);
        
        this.userSchedules.add(userSchedule);
    }
    
    public void removeUser(User user){
        if (this.userSchedules.stream().filter(us -> us.getUser().equals(user)).count() == 0){
            throw new IllegalStateException("존재하지 않는 user 입니다");
        }
        
        this.userSchedules.removeIf(userSchedule -> userSchedule.getUser().equals(user));
    }
    
    public List<String> getUserEmailList(){
        List<String> emailList = this.getUserSchedules().stream()
            .map(us -> us.getUser().getEmail())
            .collect(toList());
        
        return emailList;
    }
    
    //수정 메소드
    public void update(String name, List<User> users){
        this.name = name;
        this.userSchedules.clear();
        
        for (User user : users){
            this.addUser(user);
        }
    }
    
    public boolean hasUser(User user){
        return (this.userSchedules.stream()
                                .filter(us -> us.getUser().equals(user))
                                .count() == 1);
    }
}