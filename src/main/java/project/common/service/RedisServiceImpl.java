package project.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService{
    
    private final RedisTemplate redisTemplate;

    @Override
    public String getValues(String key){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    @Override
    public void setValues(String key, String value){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key,value);
    }

    @Override
    public void setExpiration(String key, Long time){
        int milliSeconds = time.intValue();
        
        redisTemplate.expire(key, milliSeconds, TimeUnit.MILLISECONDS);
    }

    @Override
    public void deleteByKey(String key){
        redisTemplate.delete(key);
    }
}