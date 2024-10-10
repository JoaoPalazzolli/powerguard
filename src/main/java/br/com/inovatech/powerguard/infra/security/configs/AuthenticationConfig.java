package br.com.inovatech.powerguard.infra.security.configs;

import br.com.inovatech.powerguard.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Classe de configuração de autenticação utilizando Spring Security com WebFlux.
 *
 * Define o gerenciamento de autenticação e a codificação de senhas para o contexto reativo,
 * utilizando um repositório de usuários para autenticação.
 */
@Configuration
public class AuthenticationConfig {

    @Autowired
    private UserRepository userRepository;

    /**
     * Configura um serviço reativo de detalhes de usuário que busca o usuário
     * pelo nome de usuário no repositório.
     *
     * @return ReactiveUserDetailsService Serviço reativo para carregar detalhes do usuário.
     */
    @Bean
    ReactiveUserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .cast(UserDetails.class);
    }

    /**
     * Configura o gerenciador de autenticação reativa, que utiliza o serviço de detalhes de usuário
     * e define o codificador de senhas.
     *
     * @return ReactiveAuthenticationManager Gerenciador de autenticação reativa.
     */
    @Bean
    ReactiveAuthenticationManager reactiveAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService());
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return authenticationManager;
    }

    /**
     * Define um codificador de senhas utilizando BCrypt para garantir a segurança das senhas dos usuários.
     *
     * @return PasswordEncoder Codificador de senhas BCrypt.
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
