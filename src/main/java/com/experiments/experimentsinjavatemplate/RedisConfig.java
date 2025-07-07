package com.experiments.experimentsinjavatemplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.sentinel.master}")
    private String master;

    @Value("${spring.data.redis.sentinel.nodes}")
    private String nodes;

    @Value("${spring.data.redis.password:}")
    private String password;

    @Value("${spring.data.redis.username:}")
    private String username;

    @Value("${spring.data.redis.sentinel.username:}")
    private String sentinelUsername;

    @Value("${spring.data.redis.sentinel.password:}")
    private String sentinelPassword;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        Set<String> sentinels = Stream.of(nodes.split(","))
                .collect(Collectors.toSet());
        RedisSentinelConfiguration config = new RedisSentinelConfiguration(master, sentinels);
        if (!password.isBlank()) {
            config.setPassword(RedisPassword.of(password));
        }
        if (!username.isBlank()) {
            config.setUsername(username);
        }
        if (!sentinelUsername.isBlank()) {
            config.setSentinelUsername(sentinelUsername);
        }
        if (!sentinelPassword.isBlank()) {
            config.setSentinelPassword(RedisPassword.of(sentinelPassword));
        }
        return new JedisConnectionFactory(config);
    }
}
