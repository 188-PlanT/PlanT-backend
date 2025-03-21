package project.infra.redis.application;

public interface RedisService {

    public String getValues(String key);

    public void setValues(String key, String value);

    public void setExpiration(String key, Long time);

    public void deleteByKey(String key);
}
