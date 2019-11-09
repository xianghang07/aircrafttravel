package com.travel.commons.redisManager;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 邱润泽 bullock
 */
@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    public static final String LOCK_PREFIX = "redis_lock";
    public static final int LOCK_EXPIRE = 300; // ms

    @Override
    public void put(String key, Object value, long seconds) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);

    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void renameKey(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    @Override
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void persisitKey(String key) {
        redisTemplate.persist(key);
    }


    @Override
    public void setByRedission(String key) {
        RBucket<String> keyN =  redissonClient.getBucket(key);
        keyN.set(key);
    }

    @Override
    public void Lock(String lockKey, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
    }
}