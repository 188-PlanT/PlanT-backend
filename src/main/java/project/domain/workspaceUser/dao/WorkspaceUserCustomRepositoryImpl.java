package project.domain.workspaceUser.dao;

import static project.domain.workspaceUser.domain.QWorkspaceUser.workspaceUser;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.domain.workspaceUser.domain.WorkspaceUser;

@Repository
@RequiredArgsConstructor
public class WorkspaceUserCustomRepositoryImpl implements WorkspaceUserCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<WorkspaceUser> searchByUserId(Long userId) {
        return queryFactory
                .selectFrom(workspaceUser)
                .where(workspaceUser.user.id.eq(userId))
                .innerJoin(workspaceUser.workspace)
                .fetchJoin()
                .fetch();
    }
}
