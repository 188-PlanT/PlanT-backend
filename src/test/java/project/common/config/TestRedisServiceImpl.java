package project.common.config;

import java.util.HashMap;
import java.util.Map;
import project.infra.redis.application.RedisService;

public class TestRedisServiceImpl implements RedisService {

    private final Map<String, String> customRegistry = new HashMap<>();

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
