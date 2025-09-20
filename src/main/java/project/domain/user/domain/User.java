package project.domain.user.domain;

import jakarta.persistence.*;
import java.util.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import project.domain.common.BaseEntity;
import project.domain.image.domain.Image;
import project.domain.schedule.domain.UserSchedule;
import project.domain.workspace.domain.UserWorkspace;

@Entity
@Table(name = "users")
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = true)
    private String nickName;

    @Column(nullable = true)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image profile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    // <== 유저 객체 삭제용 ==>
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<UserWorkspace> userWorkspaces = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<UserSchedule> userSchedules = new ArrayList<>();

    // <== 생성자 ==>

    // JPA용 생성자
    protected User() {}

    @Builder(access = AccessLevel.PRIVATE)
    private User(String email, String nickName, String password, Image profile) {
        this.userRole = UserRole.PENDING;
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.profile = profile;
    }

    // <== 정적 팩토리 메서드 ==>
    public static User fromOAuth2Attributes(String email, Image profile) {
        return User.builder().email(email).profile(profile).build();
    }

    public static User ofEmailPassword(String email, String password, Image profile) {
        return User.builder().email(email).password(password).profile(profile).build();
    }

    // <== 비즈니스 로직 ==>
    public String getRoleKey() {
        return this.userRole.getKey();
    }

    public boolean checkFinishSignUp() {
        return !this.userRole.equals(UserRole.PENDING);
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        this.userRole = UserRole.USER;
    }

    public void update(String nickName, String password, Image profile) {

        this.nickName = nickName != null ? nickName : this.nickName;
        this.password = password != null ? password : this.password;
        this.profile = profile != null ? profile : this.profile;
    }
}
