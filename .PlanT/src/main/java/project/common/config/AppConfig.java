package project.common.config;

import project.admin.util.LoginUserArgumentResolver;
import project.common.interceptor.*;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing //Auditing
public class AppConfig implements WebMvcConfigurer{
    
    @PersistenceContext
    private final EntityManager em;
    
    @Bean
    public JPAQueryFactory qf() {
        return new JPAQueryFactory(em);
    }
    
    //인터셉터 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LogInterceptor())
            .order(1)
            .addPathPatterns("/**")
            .excludePathPatterns("/css/**", "/*.ico","/error");
        
        registry.addInterceptor(new LoginCheckInterceptor())
            .order(2)
            .addPathPatterns("/admin/**")
            .excludePathPatterns("/admin", "/admin/users/create-users", "/admin/login", "/css/**", "/*.ico","/error");
    }
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver());
    }
}