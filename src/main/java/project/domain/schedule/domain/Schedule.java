package project.domain.schedule.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import project.domain.common.BaseEntity;
import project.domain.workspace.domain.Workspace;

@Entity
@Table(name = "schedules")
@Getter
@Slf4j
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "schedule_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Progress state; // TODO: 네이밍 변경 검토

    // < == 생성자 ==>
    protected Schedule() {} // JPA용 생성자

    @Builder(access = AccessLevel.PRIVATE)
    private Schedule(
            Workspace workspace,
            String name,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String content,
            Progress state) {
        this.workspace = workspace;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.content = content;
        this.state = state;
    }

    public static Schedule create(
            Workspace workspace, String name, LocalDateTime startDate, LocalDateTime endDate, String content) {
        return Schedule.builder()
                .workspace(workspace)
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .content(content)
                .state(Progress.TODO) // 기본값은 TO_DO
                .build();
    }

    // < == 비즈니스 로직 == >

    public void moveProgress(Progress state) {
        this.state = state;
    } // TODO: 네이밍 변경 검토

    public void update(String name, LocalDateTime startDate, LocalDateTime endDate, String content, Progress state) {
        moveProgress(state);
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.content = content;
    }
}
