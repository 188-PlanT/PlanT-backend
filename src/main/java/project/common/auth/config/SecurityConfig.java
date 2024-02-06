package project.common.auth.config;

import project.common.auth.jwt.*;
import project.common.auth.oauth.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

 
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtProvider jwtProvider;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http.csrf().disable();
        
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .formLogin().disable()
            .httpBasic().disable()
            .addFilterBefore(new CustomExceptionHandlerFilter(), OAuth2LoginAuthenticationFilter.class)
            .addFilterAfter(new JwtAuthorizationFilter(authenticationManager(), jwtProvider), OAuth2LoginAuthenticationFilter.class)
            .authorizeRequests()
            .antMatchers("/v1/login", "/v1/refresh", "/v1/users/email", "/v1/sign-up", "/v1/login/oauth2").permitAll()
            .antMatchers("/v1/users/nickname").hasAnyRole("PENDING", "USER")
            .antMatchers("/v1/**").hasRole("USER")
            .anyRequest().permitAll()
            .and()
            .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler);
    }
}

