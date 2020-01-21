package com.rocket.demo.redis;

import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 类注释
 *
 * @author WANGWEI
 */
public final class RedisClientImpl implements RedisClient {

	private RedisTemplate<String, Object> redisTemplate;

	public RedisClientImpl(RedisTemplate<String, Object> redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void set(String key, Serializable value) {
		redisTemplate.opsForValue().set(key, value);
	}

	@Override
	public void set(String key, Serializable value, int timeout) {
		set(key, value);
		expire(key, timeout);
	}

	@Override
	public void expire(String key, int timeout) {
		redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	}

	@Override
	public <T> T get(String key, Class<T> c, int timeout) {
		Object object = redisTemplate.opsForValue().get(key);
		@SuppressWarnings("unchecked")
		T t = (T) object;
		expire(key, timeout);
		return t;
	}

	@Override
	public <T> T get(String key, Class<T> c) {
		Object object = redisTemplate.opsForValue().get(key);
		@SuppressWarnings("unchecked")
		T t = (T) object;
		return t;
	}

	@Override
	public void delete(String key) {
		redisTemplate.opsForValue().set(key, null);
		expire(key, 0);
	}

	@Override
	public void convertAndSend(String channel, Object message) {
		redisTemplate.convertAndSend(channel, message);
	}

}