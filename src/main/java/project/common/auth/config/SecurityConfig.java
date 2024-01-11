package project.common.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import project.common.auth.jwt.*;
import project.common.auth.oauth.*;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import project.admin.controller.CustomFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.authentication.AuthenticationManager;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
 
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
    private final JwtProvider jwtProvider;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http.csrf().disable();
        
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .formLogin().disable()
            .httpBasic().disable()
            .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtProvider))
            .authorizeRequests()
            .antMatchers("/v1/login").permitAll()
            .antMatchers("/v1/refresh").permitAll()
            .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
            .antMatchers("/admin/**", "/css/**", "/*.ico", "/error").permitAll()
            // .anyRequest().authenticated()
            .anyRequest().permitAll()
            .and()
            .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
            .and()
            .successHandler(oAuth2AuthenticationSuccessHandler);
    }
}

