package project.domain.workspace.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import project.domain.common.BaseEntity;
import project.domain.image.domain.Image;
import project.domain.schedule.domain.Schedule;

@Slf4j
@Entity
@Table(name = "workspaces")
@Getter
public class Workspace extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "workspace_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image profile;

    // 연관관계 삭제용
    @OneToMany(mappedBy = "workspace", orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    // <== 생성자 ==>
    protected Workspace() {} // JPA용 생성자

    private Workspace(String name, Image profile) { // 기본 생성자
        this.name = name;
        this.profile = profile;
    }

    public static Workspace create(String name) {
        return new Workspace(name, null);
    }

    // 수정 로직
    public void update(String name, Image profile) {
        this.name = name;
        this.profile = profile;
    }
}
