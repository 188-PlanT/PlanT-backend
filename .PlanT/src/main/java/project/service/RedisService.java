package project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService{
    
    private final RedisTemplate redisTemplate;
    
    public String getValues(String key){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }
    
    public void setValues(String key, String value){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key,value);
    }
    
    public void setExpiration(String key, Long time){
        int second = time.intValue();
        
        redisTemplate.expire(key, second, TimeUnit.SECONDS);
    }
    
    public void deleteByKey(String key){
        redisTemplate.delete(key);
    }
}