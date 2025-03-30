package project.common.config;

import static org.springframework.security.config.Customizer.withDefaults;
import static project.common.constant.EnvironmentConstant.*;
import static project.common.constant.UrlConstant.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;
import project.common.property.BasicAuthProperty;
import project.common.security.jwt.*;
import project.common.util.EnvironmentUtil;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final CustomExceptionHandlerFilter customExceptionHandlerFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final EnvironmentUtil environmentUtil;
    private final BasicAuthProperty basicAuthProperty;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        defaultFilterChain(http);

        http.cors().configurationSource(corsConfigurationSource());

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/v1/login", "/v1/refresh", "/v1/login/oauth2", "/v1/login/dumy")
                .permitAll()
                .requestMatchers(
                        "/v1/sign-up", "/v1/users/email", "/v1/users/email/code", "/v1/users/nickname", "/v1/image")
                .permitAll()
                .requestMatchers("/admin/**", "/css/**", "*.ico")
                .permitAll()
                .requestMatchers("/v1/**")
                .hasAnyRole("USER", "ADMIN")
                .anyRequest()
                .authenticated());

        http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);
        http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);

        http.addFilterBefore(customExceptionHandlerFilter, OAuth2LoginAuthenticationFilter.class)
                .addFilterAfter(jwtAuthorizationFilter, OAuth2LoginAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(1)
    @Profile({"local", "dev"})
    public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
        defaultFilterChain(http);

        http.securityMatcher("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**")
                .httpBasic(withDefaults());

        http.authorizeHttpRequests(
                environmentUtil.getCurrentProfile() == LOCAL
                        ? authorize -> authorize.anyRequest().permitAll()
                        : authorize -> authorize.anyRequest().authenticated());

        return http.build();
    }

    private void defaultFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

    @Bean
    @Profile({"local", "dev"})
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails user = User.withUsername(basicAuthProperty.getUsername())
                .password(passwordEncoder().encode(basicAuthProperty.getPassword()))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean // 여기도 환경별 설정 해줘야함
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:8080");
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
