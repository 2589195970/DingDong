package com.ruoyi.order.config;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.order.config.properties.RedisDataSource;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.time.Duration;

/**
 * redis配置
 * 
 * @author ruoyi
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport
{


    @Bean
    public DefaultRedisScript<Long> limitScript()
    {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(limitScriptText());
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    @Primary
    @Bean(name = "configRedisDataSource")
    @ConfigurationProperties(prefix = "spring.redis.config")
    public RedisDataSource configRedisDataSource() {
        return new RedisDataSource();
    }

    @Primary
    @Bean(name = "configLettuceConnectionFactory")
    public LettuceConnectionFactory configLettuceConnectionFactory(@Qualifier("configRedisDataSource") RedisDataSource configRedisDataSource,GenericObjectPoolConfig<String> redisPool) {
        return getLettuceConnectionFactory(configRedisDataSource, redisPool);
    }

    @Primary
    @Bean(name = "configStringRedisTemplate")
    public StringRedisTemplate configStringRedisTemplate(@Qualifier("configLettuceConnectionFactory") LettuceConnectionFactory configLettuceConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(configLettuceConnectionFactory);
        return template;
    }


    @Bean(name = "addressRedisDataSource")
    @ConfigurationProperties(prefix = "spring.redis.address")
    public RedisDataSource addressRedisDataSource() {
        return new RedisDataSource();
    }


    @Bean(name = "addressLettuceConnectionFactory")
    public LettuceConnectionFactory addressLettuceConnectionFactory(@Qualifier("addressRedisDataSource") RedisDataSource configRedisDataSource,GenericObjectPoolConfig<String> redisPool) {
        return getLettuceConnectionFactory(configRedisDataSource, redisPool);
    }

    @Bean(name = "addressStringRedisTemplate")
    public StringRedisTemplate addressStringRedisTemplate(@Qualifier("addressLettuceConnectionFactory") LettuceConnectionFactory configLettuceConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(configLettuceConnectionFactory);
        return template;
    }

    @Bean(name = "temporarilyRedisDataSource")
    @ConfigurationProperties(prefix = "spring.redis.temporarily")
    public RedisDataSource temporarilyRedisDataSource() {
        return new RedisDataSource();
    }


    @Bean(name = "temporarilyLettuceConnectionFactory")
    public LettuceConnectionFactory temporarilyLettuceConnectionFactory(@Qualifier("temporarilyRedisDataSource") RedisDataSource configRedisDataSource,GenericObjectPoolConfig<String> redisPool) {
        return getLettuceConnectionFactory(configRedisDataSource, redisPool);
    }

    @Bean(name = "temporarilyStringRedisTemplate")
    public StringRedisTemplate temporarilyStringRedisTemplate(@Qualifier("temporarilyLettuceConnectionFactory") LettuceConnectionFactory configLettuceConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(configLettuceConnectionFactory);
        return template;
    }



    private LettuceConnectionFactory getLettuceConnectionFactory(RedisDataSource redisDataSource, GenericObjectPoolConfig<String> redisPool) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisDataSource.getHost());
        redisStandaloneConfiguration.setPort(redisDataSource.getPort());
        redisStandaloneConfiguration.setPassword(redisDataSource.getPassword());
        redisStandaloneConfiguration.setDatabase(redisDataSource.getDatabase());
        if(StringUtils.isNotEmpty(redisDataSource.getUsername())){
            redisStandaloneConfiguration.setUsername(redisDataSource.getUsername());
        }
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration
                .builder().commandTimeout(Duration.ofMillis(redisDataSource.getTimeout()));
        builder.poolConfig(redisPool);
        LettuceClientConfiguration lettuceClientConfiguration = builder.build();
        return new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
    }

    /**
     * 配置lettuce连接池
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.redis.lettuce.pool")
    public GenericObjectPoolConfig<String> redisPool() {
        return new GenericObjectPoolConfig<>();
    }

    /**
     * 限流脚本
     */
    private String limitScriptText()
    {
        return "local key = KEYS[1]\n" +
                "local count = tonumber(ARGV[1])\n" +
                "local time = tonumber(ARGV[2])\n" +
                "local current = redis.call('get', key);\n" +
                "if current and tonumber(current) > count then\n" +
                "    return tonumber(current);\n" +
                "end\n" +
                "current = redis.call('incr', key)\n" +
                "if tonumber(current) == 1 then\n" +
                "    redis.call('expire', key, time)\n" +
                "end\n" +
                "return tonumber(current);";
    }
}
