package br.com.alura.servico_sala.infra.redis;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.servico_sala.model.sala.DadosSala;

@Configuration
@Profile("!test")
public class RedisConfig {

        @Bean
        RedisCacheManager cacheManager(RedisConnectionFactory factory, ObjectMapper objectMapper) {
                RedisCacheConfiguration baseConfig = RedisCacheConfiguration.defaultCacheConfig()
                                .serializeKeysWith(RedisSerializationContext.SerializationPair
                                                .fromSerializer(new StringRedisSerializer()))
                                .entryTtl(Duration.ofMinutes(10));

                RedisCacheConfiguration salaConfig = baseConfig.serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                                .fromSerializer(new GenericJackson2JsonRedisSerializer()));

                var listaDadosSalaType = objectMapper.getTypeFactory().constructCollectionType(List.class,
                                DadosSala.class);
                var listaLongType = objectMapper.getTypeFactory().constructCollectionType(List.class, Long.class);

                RedisCacheConfiguration salasConfig = baseConfig.serializeValuesWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(
                                                new Jackson2JsonRedisSerializer<>(objectMapper, listaDadosSalaType)));

                RedisCacheConfiguration salasIdsConfig = baseConfig.serializeValuesWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(
                                                new Jackson2JsonRedisSerializer<>(objectMapper, listaLongType)));

                return RedisCacheManager.builder(factory)
                                .cacheDefaults(salaConfig)
                                .withInitialCacheConfigurations(Map.of(
                                                "sala", salaConfig,
                                                "salas", salasConfig,
                                                "salasIds", salasIdsConfig))
                                .build();
        }
}
