package project.domain.user.dao;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import lombok.RequiredArgsConstructor;

import com.querydsl.core.types.dsl.BooleanExpression;
import static project.domain.user.domain.QUser.user;
import com.querydsl.jpa.impl.JPAQueryFactory;
import project.domain.user.domain.User;

@Slf4j
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom{
    
    private final JPAQueryFactory qf;
    
    @Override
    public Optional<User> searchUser(String keyword){
        
        User findUser = qf.selectFrom(user)
                        .where(emailEq(keyword), nickNameEq(keyword))
                        .fetchFirst();
        
        return Optional.ofNullable(findUser);
    }
    
    // < == eq method == >
    private BooleanExpression emailEq(String email){
        if (email == null){
            return null;
        }
        return user.email.eq(email);
    }
    
    private BooleanExpression nickNameEq(String nickName){
        if (nickName == null){
            return null;
        }
        return user.nickName.eq(nickName);
    }
}