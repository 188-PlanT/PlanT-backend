package project.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import project.common.service.RedisService;

@Configuration
public class TestRedisConfig {
    @Bean
    @Profile("test")
    public RedisService redisService() {
        return new TestRedisServiceImpl();
    }
}
