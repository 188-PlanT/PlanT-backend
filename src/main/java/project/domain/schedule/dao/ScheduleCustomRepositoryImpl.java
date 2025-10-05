package project.domain.schedule.dao;

import static project.domain.schedule.domain.QSchedule.schedule;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.domain.schedule.domain.Schedule;

@Repository
@RequiredArgsConstructor
public class ScheduleCustomRepositoryImpl implements ScheduleCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Schedule> searchByWorkspaceInAndDateBetween(
            List<Long> workspaceIds, LocalDateTime startDate, LocalDateTime endDate) {
        return queryFactory
                .selectFrom(schedule)
                .where(schedule.workspace.id.in(workspaceIds), dateCondition(startDate, endDate))
                .fetch();
    }

    private BooleanExpression dateCondition(LocalDateTime startDate, LocalDateTime endDate) {
        return schedule.startDate.loe(endDate).and(schedule.endDate.goe(startDate));
    }
}
