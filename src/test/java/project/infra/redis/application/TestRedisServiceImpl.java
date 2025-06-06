package project.infra.redis.application;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestRedisServiceImpl implements RedisService {

    private final Map<String, String> customRegistry = new ConcurrentHashMap<>();

    @Override
    public String getValues(String key) {
        return customRegistry.get(key);
    }

    @Override
    public void setValues(String key, String value) {
        customRegistry.put(key, value);
    }

    @Override
    public void setExpiration(String key, Long time) {
        customRegistry.get(key);
    }

    @Override
    public void deleteByKey(String key) {
        customRegistry.remove(key);
    }
}
