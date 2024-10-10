package br.com.inovatech.powerguard.services;

import br.com.inovatech.powerguard.domains.UserDomain;
import br.com.inovatech.powerguard.dtos.SigninDTO;
import br.com.inovatech.powerguard.dtos.TokenDTO;
import br.com.inovatech.powerguard.infra.configs.CacheEnvironmentConfig;
import br.com.inovatech.powerguard.infra.security.configs.AuthenticationEnvironmentConfig;
import br.com.inovatech.powerguard.infra.security.roles.UserRoles;
import br.com.inovatech.powerguard.infra.security.utils.JwtUtils;
import br.com.inovatech.powerguard.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Serviço de autenticação responsável pelo login (signin) e inicialização
 * de usuários com suas respectivas permissões e chaves de cache.
 *
 * Utiliza ReactiveAuthenticationManager para realizar a autenticação
 * reativa e JWT para gerar tokens de acesso.
 */
@Slf4j
@Service
public class AuthService {

    @Autowired
    private ReactiveAuthenticationManager reactiveAuthenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CacheEnvironmentConfig cacheKeys;

    @Autowired
    private AuthenticationEnvironmentConfig authenticationEnvironments;

    /**
     * Autentica um usuário baseado nas credenciais fornecidas no SigninDTO.
     * Se o usuário for autenticado com sucesso, um token JWT será gerado e retornado.
     *
     * @param signinDTO Objeto que contém o nome de usuário e a senha.
     * @return Mono<ResponseEntity<TokenDTO>> que contém o token JWT de autenticação.
     */
    public Mono<ResponseEntity<TokenDTO>> signin(SigninDTO signinDTO) {
        log.info("Efetuando Login");
        return userRepository.findByUsername(signinDTO.getUsername())
                .switchIfEmpty(Mono.error(new BadCredentialsException("invalid username or password!!!")))
                .flatMap(user -> reactiveAuthenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(signinDTO.getUsername(), signinDTO.getPassword()))
                        .thenReturn(jwtUtils.createToken(user)))
                .map(ResponseEntity::ok);
    }

    /**
     * Método executado após a construção da classe (após injeção de dependências),
     * responsável por cadastrar usuários padrão definidos no arquivo de configuração
     * de autenticação. Cada usuário recebe um papel (role), uma chave de cache e uma
     * senha codificada.
     */
    @PostConstruct
    private void signup(){
        for(int i = 0; i < UserRoles.values().length; i++){
            var role = UserRoles.values()[i];
            var username = authenticationEnvironments.getUsernames()[i];
            var password = authenticationEnvironments.getPasswords()[i];
            var cacheKey = cacheKeys.getCaches()[i];

            var user = UserDomain.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .userRoles(role)
                    .keyRequest(cacheKey)
                    .build();

            userRepository.save(user).subscribe();
        }
    }

}
