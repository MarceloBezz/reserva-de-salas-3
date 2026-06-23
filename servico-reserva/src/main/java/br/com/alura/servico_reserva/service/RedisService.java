package br.com.alura.servico_reserva.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final ReactiveStringRedisTemplate redis;
    private final ObjectMapper objectMapper;

    private static final Logger log = LoggerFactory.getLogger(RedisService.class);

    private final MeterRegistry meterRegistry;

    public Mono<Void> limparCache(String chave) {
        return redis
                .keys(chave)
                .flatMap(redis::delete)
                .doOnNext(x -> log.info("Chave deletada: [{}]",x))
                .then();
    }

    public Mono<List<Long>> buscarNoCache(String chave) {
        return redis.opsForValue()
                .get(chave)
                .map(this::desserializar)
                .doOnNext(x ->
                        log.info("REDIS - CACHE HIT [{}]", chave))
                .doOnNext(x -> meterRegistry.counter("redis.hit").increment());
    }

    public Mono<Boolean> salvarNoCache(String chave, List<Long> valor, int tempo) {
        return redis
                .opsForValue()
                .set(chave, serializar(valor), Duration.ofMinutes(tempo));
    }

    private String serializar(List<Long> lista) {
        try {
            return objectMapper.writeValueAsString(lista);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Long> desserializar(String json) {
        try {
            return objectMapper.readValue(
                    json,
                    new TypeReference<List<Long>>() {
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
