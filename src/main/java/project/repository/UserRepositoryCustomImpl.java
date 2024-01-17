package project.repository;

import java.util.List;
import project.domain.*;
import lombok.RequiredArgsConstructor;
import com.querydsl.core.types.dsl.BooleanExpression;
import static project.domain.QUser.user;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom{
    
    private final JPAQueryFactory qf;
    
    @Override
    public Page<User> searchUsers(Pageable pageable, String email){
        
        List<User> content =  qf.selectFrom(user)
            .where(emailEq(email))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();  
        
        Long count = qf.select(user.count())
            .from(user)
            .where(emailEq(email))
            .fetchOne();
        
        return new PageImpl<>(content, pageable, count);
    }
    
    // < == eq method == >
    private BooleanExpression emailEq(String email){
        if (email == null){
            return null;
        }
        return user.email.eq(email);
    }
    
    // private BooleanExpression nameEq(String name){
    //     if (name == null){
    //         return null;
    //     }
    //     return user.name.eq(name);
    // }
}