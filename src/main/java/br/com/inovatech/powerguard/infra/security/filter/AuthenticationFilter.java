package br.com.inovatech.powerguard.infra.security.filter;

import br.com.inovatech.powerguard.infra.security.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Filtro de autenticação JWT para WebFlux que intercepta requisições e valida o token JWT
 * para autenticar o usuário em cada requisição.
 *
 * Esta classe implementa a interface `WebFilter`, integrando a autenticação JWT ao contexto de segurança reativo do Spring.
 */
@Slf4j
@Component
public class AuthenticationFilter implements WebFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ReactiveUserDetailsService userDetailsService;

    /**
     * Método que intercepta todas as requisições para verificar a presença e validade de um token JWT.
     * Caso o token seja válido, um `UsernamePasswordAuthenticationToken` é criado e adicionado ao contexto
     * de segurança reativo.
     *
     * @param exchange Representa a requisição do cliente e a resposta do servidor.
     * @param chain Cadeia de filtros que a requisição deve passar.
     * @return Mono<Void> Continua a cadeia de filtros ou processa a autenticação do usuário.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = extractToken(exchange);

        if (token == null) {
            return chain.filter(exchange); // Continua se não houver token
        }

        String username = jwtUtils.extractUsername(token);

        if (username == null) {
            return chain.filter(exchange); // Continua se não conseguir extrair o username
        }

        return userDetailsService.findByUsername(username)
                .flatMap(userDetails -> {
                    if (jwtUtils.isTokenValid(token, userDetails)) {
                        // Autentica o usuário se o token for válido
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        SecurityContext securityContext = new SecurityContextImpl(authentication);

                        return Mono.deferContextual(Mono::just)
                                .flatMap(context -> chain.filter(exchange)
                                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext))));
                    }

                    log.error("Authentication error: Invalid or expired JWT token.");
                    return chain.filter(exchange); // Continua se o token não for válido
                });
    }

    /**
     * Método auxiliar para extrair o token JWT do cabeçalho Authorization da requisição.
     *
     * @param exchange O objeto que contém a requisição HTTP.
     * @return String O token JWT se presente, ou null se o cabeçalho não contiver um token válido.
     */
    private String extractToken(ServerWebExchange exchange) {
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring("Bearer ".length());  // Remove "Bearer " para obter o token
        }
        return null;
    }
}
