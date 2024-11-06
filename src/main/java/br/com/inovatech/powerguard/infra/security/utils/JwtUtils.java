package br.com.inovatech.powerguard.infra.security.utils;

import br.com.inovatech.powerguard.dtos.TokenDTO;
import br.com.inovatech.powerguard.infra.exceptions.InvalidJwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Classe utilitária para geração, extração e validação de tokens JWT.
 * Utiliza a biblioteca JJWT para assinar, gerar e validar os tokens.
 *
 * As chaves e o tempo de expiração dos tokens são configurados por meio de variáveis
 * externas definidas no arquivo de propriedades da aplicação.
 */
@Component
public class JwtUtils {

    @Value("${security.key}")
    private String securityKey;

    @Value("${security.token_expiration_time}")
    private long tokenExpirationTime;

    /**
     * Extrai o nome de usuário (subject) do token JWT.
     *
     * @param token O token JWT do qual será extraído o nome de usuário.
     * @return O nome de usuário contido no token.
     */
    public String extractUsername(String token){
        return extractClaim(Claims::getSubject, token);
    }

    /**
     * Atualiza o token JWT de um usuário, verificando se o token atual ainda é válido.
     *
     * @param refreshToken O token de atualização atual.
     * @param userDetails Detalhes do usuário para quem o novo token será gerado.
     * @return Um novo TokenDTO com o accessToken e refreshToken atualizados.
     */
    public TokenDTO refreshToken(String refreshToken, UserDetails userDetails){
        return refreshToken(new HashMap<>(), refreshToken, userDetails);
    }

    /**
     * Atualiza o token JWT com claims adicionais, verificando a validade do token atual.
     *
     * @param extraClaims Claims adicionais a serem incluídos no novo token.
     * @param refreshToken O token de atualização atual.
     * @param userDetails Detalhes do usuário para quem o novo token será gerado.
     * @return Um novo TokenDTO com o accessToken e refreshToken atualizados.
     */
    public TokenDTO refreshToken(Map<String, Object> extraClaims, String refreshToken, UserDetails userDetails){
        if(refreshToken.contains("Bearer ")){
            refreshToken = refreshToken.substring("Bearer ".length());
        }

        if(!isTokenValid(refreshToken, userDetails)){
            throw new InvalidJwtAuthenticationException("Invalid Token!!!");
        }

        return createToken(extraClaims, userDetails);
    }

    /**
     * Cria um TokenDTO que contém o accessToken e refreshToken a partir dos detalhes do usuário.
     *
     * @param userDetails Detalhes do usuário para os quais o token será gerado.
     * @return Um objeto TokenDTO contendo o accessToken e refreshToken.
     */
    public TokenDTO createToken(UserDetails userDetails){
        return createToken(new HashMap<>(), userDetails);
    }

    /**
     * Cria um TokenDTO com claims extras e os detalhes do usuário, contendo o accessToken e refreshToken.
     *
     * @param extraClaims Mapa de claims adicionais para serem incluídos no token.
     * @param userDetails Detalhes do usuário para os quais o token será gerado.
     * @return Um objeto TokenDTO contendo o accessToken e refreshToken.
     */
    public TokenDTO createToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return TokenDTO.builder()
                .accessToken(generateToken(extraClaims, userDetails, tokenExpirationTime * 24))  // Access Token
                .refreshToken(generateToken(extraClaims, userDetails, tokenExpirationTime * 36))  // Refresh Token
                .build();
    }

    /**
     * Gera um token JWT assinado com claims e tempo de expiração.
     *
     * @param extraClaims Claims extras a serem incluídos no token.
     * @param userDetails Detalhes do usuário que será autenticado com o token.
     * @param expirationTime Tempo de expiração do token em milissegundos.
     * @return O token JWT gerado.
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, long expirationTime){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))  // Definindo a expiração
                .signWith(getSignKey(), SignatureAlgorithm.HS512)  // Assinatura com HS512
                .compact();
    }

    /**
     * Extrai uma claim específica de um token JWT utilizando uma função fornecida.
     *
     * @param claimsFunction Função para aplicar à claim.
     * @param token O token JWT do qual a claim será extraída.
     * @param <T> Tipo do retorno da claim.
     * @return A claim extraída do token.
     */
    private <T> T extractClaim(Function<Claims, T> claimsFunction, String token){
        var claim = extractAllClaims(token);
        return claimsFunction.apply(claim);
    }

    /**
     * Extrai todas as claims de um token JWT.
     *
     * @param token O token JWT do qual as claims serão extraídas.
     * @return As claims contidas no token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())  // Chave de assinatura
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Retorna a chave de assinatura HMAC usada para assinar os tokens JWT.
     *
     * @return A chave secreta usada para assinar o token JWT.
     */
    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(securityKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Valida se um token JWT é válido para um determinado usuário.
     *
     * @param token O token JWT a ser validado.
     * @param user Detalhes do usuário para verificar a validade.
     * @return True se o token for válido e pertencer ao usuário fornecido.
     */
    public boolean isTokenValid(String token, UserDetails user) {
        return extractUsername(token).equals(user.getUsername()) && !isTokenExpirated(token);
    }

    /**
     * Verifica se um token JWT está expirado.
     *
     * @param token O token JWT a ser verificado.
     * @return True se o token estiver expirado.
     */
    private boolean isTokenExpirated(String token){
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrai a data de expiração de um token JWT.
     *
     * @param token O token JWT do qual a data de expiração será extraída.
     * @return A data de expiração do token.
     */
    private Date extractExpiration(String token){
        return extractClaim(Claims::getExpiration, token);
    }
}

