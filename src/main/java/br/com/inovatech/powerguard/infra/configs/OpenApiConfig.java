package br.com.inovatech.powerguard.infra.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("PowerGuard API - Sistema de Gerenciamento de Energia")
                        .version("v1")
                        .description("O Powerguard API oferece um conjunto de endpoints para monitorar e gerenciar a energia nos prédios A, C e L. " +
                                "Desenvolvida em Java 17 com Spring Boot 3.3.4, a API inclui funcionalidades como monitoramento de consumo e " +
                                "relatórios detalhados de desempenho energético.")
                        .termsOfService("null")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
