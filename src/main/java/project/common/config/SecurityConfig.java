package project.common.config;

import project.common.security.jwt.*;
import project.common.security.oauth.*;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.web.cors.*;
import org.springframework.beans.factory.annotation.Value;

 
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
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtProvider jwtProvider;
	
	@Value("${front.main-url}")
	private String MAIN_URL;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http.csrf().disable();
        http.headers().frameOptions().disable();
        
        http
            .httpBasic().disable()
            .formLogin().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .addFilterBefore(new CustomExceptionHandlerFilter(), OAuth2LoginAuthenticationFilter.class)
            .addFilterAfter(new JwtAuthorizationFilter(authenticationManager(), jwtProvider), OAuth2LoginAuthenticationFilter.class)
            .authorizeRequests()
            .antMatchers("/v1/login", "/v1/refresh", "/v1/users/email", "/v1/sign-up", "/v1/login/oauth2", "/v1/login/dumy", "/v1/users/email/code", "/v1/image").permitAll()
            .antMatchers("/admin/**", "/css/**", "*.ico").permitAll()
            .antMatchers("/v1/users/nickname").hasAnyRole("PENDING", "USER", "ADMIN")
            .antMatchers("/v1/**").hasAnyRole("USER", "ADMIN") //여기 런칭할때는 수정해야함
            .anyRequest().authenticated()
            .and()
            .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler);
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("https://plant-front-bwmaj.run.goorm.site");
        configuration.addAllowedOrigin(MAIN_URL);
	    configuration.addAllowedOrigin("https://blazingdevs-calendar-ubvam.run.goorm.io");
        configuration.addAllowedOrigin("http://127.0.0.1:8080");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

