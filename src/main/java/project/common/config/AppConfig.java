package project.common.config;

import project.common.admin.util.LoginUserArgumentResolver;
import project.common.interceptor.auth.UserRoleCheckInterceptor;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import project.common.interceptor.log.LogInterceptor;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing //Auditing
public class AppConfig implements WebMvcConfigurer{
	
	private final UserRoleCheckInterceptor userRoleCheckInterceptor;
    //인터셉터 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LogInterceptor())
            .order(1)
            .addPathPatterns("/**")
            .excludePathPatterns("/css/**", "/*.ico","/error");
		
		registry.addInterceptor(userRoleCheckInterceptor)
            .order(2)
            .addPathPatterns("/**")
            .excludePathPatterns("/css/**", "/*.ico","/error");
        
        // registry.addInterceptor(new LoginCheckInterceptor())
        //     .order(2)
        //     .addPathPatterns("/admin/**")
        //     .excludePathPatterns("/admin", "/admin/users/create-users", "/admin/login", "/css/**", "/*.ico","/error");
    }
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver());
    }
}