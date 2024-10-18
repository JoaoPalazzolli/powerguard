package br.com.inovatech.powerguard.infra.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.cache-key")
@Data
public class CacheEnvironmentConfig {

    private String keyA;
    private String keyC;
    private String keyL;

    public String[] getCaches(){
        return new String[] {keyA, keyC, keyL};
    }
}
