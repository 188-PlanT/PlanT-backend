package project.common.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.SecurityFilterChain;
import project.common.security.jwt.*;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.web.cors.*;

import static project.common.constant.UrlConstant.*;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final CustomExceptionHandlerFilter customExceptionHandlerFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.cors().configurationSource(corsConfigurationSource());

        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v1/login", "/v1/refresh", "/v1/login/oauth2", "/v1/login/dumy")
                        .permitAll()
                        .requestMatchers( "/v1/sign-up", "/v1/users/email", "/v1/users/email/code", "/v1/users/nickname", "/v1/image")
                        .permitAll()
                        .requestMatchers("/admin/**", "/css/**", "*.ico")
                        .permitAll()
                        .requestMatchers("/v1/**")
                        .hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                );

        http.exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler);

        http.addFilterBefore(customExceptionHandlerFilter, OAuth2LoginAuthenticationFilter.class)
                .addFilterAfter(jwtAuthorizationFilter, OAuth2LoginAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin(FRONT_LOCAL_URL);
        configuration.addAllowedOrigin(FRONT_DEV_URL);
        configuration.addAllowedOrigin(FRONT_PROD_URL);
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        CorsConfiguration adminConfig = new CorsConfiguration();
        adminConfig.addAllowedOriginPattern("*");
        adminConfig.addAllowedHeader("*");
        adminConfig.addAllowedMethod("*");
        adminConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/v1/**", configuration);
        source.registerCorsConfiguration("/admin/**", adminConfig);
        return source;
    }
}

