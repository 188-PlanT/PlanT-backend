package project.domain.schedule.dao;

import java.util.List;

import lombok.RequiredArgsConstructor;
import static project.domain.schedule.domain.QSchedule.schedule;
import static project.domain.schedule.domain.QUserSchedule.userSchedule;
import static project.domain.workspace.domain.QWorkspace.workspace;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.dsl.BooleanExpression;
import project.domain.schedule.domain.Schedule;


//이건 그냥 전용 리포지토리 만드는게 좋아보임
@RequiredArgsConstructor
public class ScheduleRepositoryCustomImpl implements ScheduleRepositoryCustom{
    
    private final JPAQueryFactory qf;
    
    @Override
    public List<Schedule> searchSchedulesByUserEmail(String email){

        return qf.selectFrom(schedule)
            .join(schedule.workspace, workspace).fetchJoin()
            .join(schedule.userSchedules, userSchedule)
            .where(userEq(email))
            .distinct()
            .fetch();
    }
    
    private BooleanExpression userEq(String email){
        if (email == null){
            return null;
        }
        return userSchedule.user.email.eq(email);
    }
}