package br.com.inovatech.powerguard.infra.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Classe de configuração do Redis reativo.
 *
 * Esta classe define o bean para a configuração de um `ReactiveRedisTemplate`, que é usado para
 * a interação reativa com o banco de dados Redis.
 *
 * - O `ReactiveRedisTemplate` é configurado com serializadores para as chaves e valores.
 * - As chaves são serializadas como strings usando `StringRedisSerializer`.
 * - Os valores são serializados em JSON utilizando o `Jackson2JsonRedisSerializer`.
 */
@Configuration
public class RedisConfig {

    /**
     * Configura o `ReactiveRedisTemplate` com serialização para trabalhar com Redis de forma reativa.
     *
     * @param factory A fábrica de conexões reativas do Redis.
     * @return ReactiveRedisTemplate<String, Object> Template reativo configurado para interagir com o Redis.
     */
    @Bean
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

        RedisSerializationContext<String, Object> context = RedisSerializationContext
                .<String, Object>newSerializationContext(new StringRedisSerializer())
                .value(serializer)
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
