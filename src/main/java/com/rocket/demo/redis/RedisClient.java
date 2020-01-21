package com.rocket.demo.redis;

import java.io.Serializable;

/**
 * 类注释
 *
 * @author WANGWEI
 */
public interface RedisClient {

	/**
	 * 方法注释
	 *
	 * @author WANGWEI
	 * @param key
	 * @param value
	 */
	public void set(String key, Serializable value);

	/**
	 * 方法注释
	 *
	 * @author WANGWEI
	 * @param key
	 * @param value
	 * @param timeout
	 */
	public void set(String key, Serializable value, int timeout);

	/**
	 * 方法注释
	 *
	 * @author WANGWEI
	 * @param key
	 * @param timeout
	 */
	public void expire(String key, int timeout);

	/**
	 * 方法注释
	 *
	 * @author WANGWEI
	 * @param key
	 * @param c
	 * @param timeout
	 * @return
	 */
	public <T> T get(String key, Class<T> c, int timeout);

	/**
	 * 方法注释
	 *
	 * @author WANGWEI
	 * @param key
	 * @param c
	 * @return
	 */
	public <T> T get(String key, Class<T> c);

	/**
	 * 方法注释
	 *
	 * @author WANGWEI
	 * @param key
	 */
	public void delete(String key);

	/**
	 * 方法注释
	 *
	 * @author WANGWEI
	 * @param channel
	 * @param message
	 */
	public void convertAndSend(String channel, Object message);

}