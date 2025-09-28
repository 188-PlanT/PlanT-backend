package project.domain.scheduleUser.dao;

import static project.domain.scheduleUser.domain.QScheduleUser.scheduleUser;

import com.querydsl.jpa.impl.JPAQueryFactory;
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
}
