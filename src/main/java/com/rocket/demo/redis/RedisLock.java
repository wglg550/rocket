package com.rocket.demo.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Description: 利用jedis+redisTemplate结合实现严谨的redis分布式锁，因为jedis的set方法具有原子性，而使用redisTemplate需要两步操作(set，expire)
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/2/20
 */
@Component
public class RedisLock {
    public static final Logger LOGGER = LoggerFactory.getLogger(RedisLock.class);

    private static final Long RELEASE_SUCCESS = 1L;
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    //expx EX|PX, expire time units: EX = seconds; PX = milliseconds
    private static final String SET_WITH_EXPIRE_TIME = "EX";
    private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    private static final TimeUnit REDIS_LOCK_TIME_UNIT_S = TimeUnit.SECONDS;

    @Autowired
    private RedisTemplate redisTemplate;

//    @Autowired
//    private JedisClient jedisClient;

    /**
     * 该加锁方法仅针对单实例Redis可实现分布式加锁
     * redis主从也可以适用
     * 对于 Redis 集群则无法使用
     * 支持重复，线程安全
     * 后改用redisTemplate.set方法一次加锁
     *
     * @param key     加锁键
     * @param value   加锁客户端唯一标识(采用UUID)
     * @param seconds 锁过期时间
     * @return
     */
    public boolean tryLock(String key, String value, long seconds) {
        Object result = redisTemplate.opsForValue().get(key);
        if (result == null) {
            redisTemplate.opsForValue().set(key, value, seconds, REDIS_LOCK_TIME_UNIT_S);
            return true;
        } else
            return false;
//        return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
//            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
//            System.out.println(jedis.get(lockKey));
//            String result = jedis.set(lockKey, clientId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, seconds);
//            if (LOCK_SUCCESS.equals(result))
//                return Boolean.TRUE;
//            return Boolean.FALSE;
//        });
    }

    /**
     * 与tryLock相对应，用作释放锁
     *
     * @param key
     * @return
     */
    public boolean releaseLock(String key) {
//        return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
//            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
//            Object result = jedis.eval(RELEASE_LOCK_SCRIPT, Collections.singletonList(lockKey),
//                    Collections.singletonList(clientId));
//            if (RELEASE_SUCCESS.equals(result))
//                return Boolean.TRUE;
//            return Boolean.FALSE;
//        });
        Object result = redisTemplate.opsForValue().get(key);
        if (result != null) {
            redisTemplate.delete(key);
            return true;
        } else
            return false;
    }
}
