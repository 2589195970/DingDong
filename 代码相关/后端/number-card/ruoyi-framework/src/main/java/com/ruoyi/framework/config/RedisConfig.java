package com.ruoyi.framework.config;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.properties.RedisDataSource;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

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
    /*@Bean
    @SuppressWarnings(value = { "unchecked", "rawtypes" })
    @Primary
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory)
    {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        // Hash的key也采用StringRedisSerializer的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }*/
    @Primary
    @Bean(name = "publicRedisDataSource")
    @ConfigurationProperties(prefix = "spring.redis.public")
    public RedisDataSource publicRedisDataSource() {
        return new RedisDataSource();
    }

    @Primary
    @Bean(name = "publicLettuceConnectionFactory")
    public LettuceConnectionFactory publicLettuceConnectionFactory(@Qualifier("publicRedisDataSource") RedisDataSource configRedisDataSource,GenericObjectPoolConfig<String> redisPool) {
        return getLettuceConnectionFactory(configRedisDataSource, redisPool);
    }

    @Bean(name = "defaultStringRedisTemplate")
    @Primary
    public RedisTemplate<Object, Object> publicStringRedisTemplate(@Qualifier("publicLettuceConnectionFactory") LettuceConnectionFactory publicLettuceConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(publicLettuceConnectionFactory);
        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);
        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        // Hash的key也采用StringRedisSerializer的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }


    @Bean
    public DefaultRedisScript<Long> limitScript()
    {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(limitScriptText());
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    @Bean(name = "configRedisDataSource")
    @ConfigurationProperties(prefix = "spring.redis.config")
    public RedisDataSource configRedisDataSource() {
        return new RedisDataSource();
    }

    @Bean(name = "configLettuceConnectionFactory")
    public LettuceConnectionFactory configLettuceConnectionFactory(@Qualifier("configRedisDataSource") RedisDataSource configRedisDataSource,GenericObjectPoolConfig<String> redisPool) {
        return getLettuceConnectionFactory(configRedisDataSource, redisPool);
    }

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
