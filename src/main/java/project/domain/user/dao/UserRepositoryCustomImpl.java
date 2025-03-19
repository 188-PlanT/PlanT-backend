package project.domain.user.dao;

import static project.domain.user.domain.QUser.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.domain.user.domain.User;
import project.domain.user.domain.UserRole;

@Slf4j
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory qf;

    @Override
    public Optional<User> searchUser(String keyword) {

        User findUser =
                qf.selectFrom(user).where(emailEq(keyword), nickNameEq(keyword)).fetchFirst();

        return Optional.ofNullable(findUser);
    }

    // < == eq method == >
    private BooleanExpression emailEq(String email) {
        if (email == null) {
            return null;
        }
        return user.email.eq(email);
    }

    private BooleanExpression nickNameEq(String nickName) {
        if (nickName == null) {
            return null;
        }
        return user.nickName.eq(nickName);
    }

    @Override
    public List<User> searchByKeyword(Long loginUserId, String keyword) {
        return qf.selectFrom(user)
                .where(neLoginUser(loginUserId).and(containKeyword(keyword)).and(notPending()))
                .fetch();
    }

    private BooleanExpression neLoginUser(Long loginUserId) {
        return user.id.ne(loginUserId);
    }

    private BooleanExpression containKeyword(String keyword) {
        return user.nickName.contains(keyword).or(user.email.contains(keyword));
    }

    private BooleanExpression notPending() {
        return user.userRole.ne(UserRole.PENDING);
    }
}
