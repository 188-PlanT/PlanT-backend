package project.infra.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import project.infra.redis.application.RedisService;
import project.infra.redis.application.TestRedisServiceImpl;

@Configuration
public class TestRedisConfig {
    @Bean
    @Profile("test")
    public RedisService redisService() {
        return new TestRedisServiceImpl();
    }
}
