package br.com.inovatech.powerguard.infra.security.utils;

import br.com.inovatech.powerguard.domains.UserDomain;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Mono;

/**
 * Classe utilitária para obter informações do usuário autenticado no contexto de segurança reativa.
 */
public class AuthenticatedUserUtils {

    /**
     * Obtém o usuário autenticado a partir do contexto de segurança reativo.
     *
     * @return Um Mono contendo o usuário autenticado (UserDomain) ou vazio se não houver usuário autenticado.
     */
    public static Mono<UserDomain> getUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> (UserDomain) securityContext.getAuthentication().getPrincipal());
    }
}
