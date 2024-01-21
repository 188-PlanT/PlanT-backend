package project.repository;

import project.domain.*;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

import com.querydsl.core.types.dsl.BooleanExpression;
import static project.domain.QUser.user;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.dsl.BooleanExpression;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom{
    
    private final JPAQueryFactory qf;
    
    @Override
    public Optional<User> searchUser(String email, String nickName){
        
        User findUser = qf.selectFrom(user)
                        .where(emailEq(email), nickNameEq(nickName))
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