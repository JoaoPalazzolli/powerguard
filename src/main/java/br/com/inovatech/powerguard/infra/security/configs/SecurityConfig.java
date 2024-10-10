package br.com.inovatech.powerguard.infra.security.configs;

import br.com.inovatech.powerguard.infra.security.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Classe de configuração de segurança para a aplicação utilizando Spring Security com WebFlux.
 * Define como as requisições são autorizadas, configurando filtros de autenticação e a integração
 * com um `ReactiveAuthenticationManager`.
 */
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private AuthenticationFilter authFilter;

    @Autowired
    private ReactiveAuthenticationManager reactiveAuthenticationManager;

    /**
     * Configura a cadeia de filtros de segurança da aplicação, desabilitando a proteção CSRF,
     * permitindo acesso irrestrito a certos endpoints e exigindo autenticação para outros.
     *
     * @param http Instância de `ServerHttpSecurity` utilizada para configurar a segurança HTTP.
     * @return SecurityWebFilterChain A cadeia de filtros de segurança configurada.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/v1/auth/**").permitAll() // Endpoints de autenticação acessíveis sem login
                        .pathMatchers("/api/v1/energy/**").authenticated() // Endpoints de energia exigem autenticação
                )
                .authenticationManager(reactiveAuthenticationManager) // Gerenciador de autenticação reativo
                .addFilterAt(authFilter, SecurityWebFiltersOrder.AUTHENTICATION) // Adiciona o filtro de autenticação personalizado
                .build();
    }
}

