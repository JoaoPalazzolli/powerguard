package br.com.inovatech.powerguard.infra.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.cache-key")
@Data
public class CacheEnvironmentConfig {

    private String cacheKeyA;
    private String cacheKeyC;
    private String cacheKeyL;

    public String[] getCaches(){
        return new String[] {cacheKeyA, cacheKeyC, cacheKeyL};
    }
}
