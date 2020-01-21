package com.rocket.demo.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

//@Configuration
public class JedisConfig {
    public static final Logger LOGGER = LoggerFactory.getLogger(JedisConfig.class);

    @Value("${redis.server.timeOut}")
    private int timeOut;

    @Value("${redis.server.maxIdle}")
    private int maxIdle;

    @Value("${redis.server.maxWaitMillis}")
    private int maxWaitMillis;

    @Value("${redis.server.maxTotal}")
    private int maxTotal;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        return new JedisPool(config, host, port, timeOut);
    }

    @Bean
    public JedisClient jedisClient(JedisPool jedisPool) {
        LOGGER.info("初始化……Redis Client==Host={},Port={}", host, port);
        JedisClient jedisClient = new JedisClient();
        jedisClient.setJedisPool(jedisPool);
        return jedisClient;
    }
}
