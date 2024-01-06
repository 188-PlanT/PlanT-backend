package project.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.util.*;
import static java.util.stream.Collectors.toList;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;


@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User extends BaseEntity{
    
    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    
    @Column(unique = false)
    private String email;
    
    @Column(nullable = true)
    private String password;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true)
    private String nickName;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserWorkspace> userWorkspaces = new ArrayList<> ();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSchedule> userSchedules = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DevLog> devLogs = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;
    
    public String getRoleKey(){
        return this.userRole.getKey();
    }
    
    @Builder
    public User(String email, String password, String name, UserRole userRole){
        this.password = password;
        this.name = name;
        this.email = email;
        this.userRole = userRole;
    }
    
    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
    }
    
    public void update(String password, String name, PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
        this.name = name;
    }
    
    public static User fromOAuth2Attributes(Map<String,Object> attributes){
        return User.builder()
            .name((String) attributes.get("name"))
            .email((String) attributes.get("email"))
            .userRole(UserRole.USER)
            .build();
    }
}