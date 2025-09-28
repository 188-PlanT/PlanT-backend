package project.domain.scheduleUser.dao;

import static project.domain.schedule.domain.QSchedule.schedule;
import static project.domain.scheduleUser.domain.QScheduleUser.scheduleUser;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.domain.scheduleUser.domain.ScheduleUser;

@Repository
@RequiredArgsConstructor
public class ScheduleUserCustomRepositoryImpl implements ScheduleUserCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ScheduleUser> findFetchByScheduleId(Long scheduleId) {
        return queryFactory
                .selectFrom(scheduleUser)
                .join(scheduleUser.user)
                .fetchJoin()
                .where(scheduleUser.schedule.id.eq(scheduleId))
                .fetch();
    }

    @Override
    public List<ScheduleUser> searchByUserAndDate(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return queryFactory
                .selectFrom(scheduleUser)
                .join(scheduleUser.schedule)
                .fetchJoin()
                .join(schedule.workspace)
                .fetchJoin()
                .where(scheduleUser.user.id.eq(userId), dateCondition(startDate, endDate))
                .fetch();
    }

    private BooleanExpression dateCondition(LocalDateTime startDate, LocalDateTime endDate) {
        return schedule.startDate.loe(endDate).and(schedule.endDate.goe(startDate));
    }
}
