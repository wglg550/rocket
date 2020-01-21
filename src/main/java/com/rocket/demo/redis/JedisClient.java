package com.rocket.demo.redis;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

//@Component
public class JedisClient {
    private static final String SET_IF_NOT_EXIST = "NX";
    //expx EX|PX, expire time units: EX = seconds; PX = milliseconds
    private static final String SET_WITH_EXPIRE_TIME = "EX";
    private JedisPool jedisPool;

    /**
     * 设置key
     *
     * @param key
     * @param value
     */
    public String set(String key, Object value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.set(key, value.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param value
     * @param exptime
     */
    public String setWithExpireTime(String key, String value, int exptime) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.set(key, value, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, exptime);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * 根据key取值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return null;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
