package project.common.config;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Configuration
@RequiredArgsConstructor
public class QueryDslConfig{
    
    @PersistenceContext
    private final EntityManager em;
    
    @Bean
    public JPAQueryFactory qf() {
        return new JPAQueryFactory(em);
    }
}