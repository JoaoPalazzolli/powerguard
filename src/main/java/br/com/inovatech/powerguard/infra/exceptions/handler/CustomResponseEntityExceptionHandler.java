package br.com.inovatech.powerguard.infra.exceptions.handler;

import br.com.inovatech.powerguard.infra.exceptions.ExceptionsResponse;
import br.com.inovatech.powerguard.infra.exceptions.ExternalApiRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ExceptionsResponse>> handleAllExceptions(Exception ex, ServerWebExchange exchange) {

        var exceptionsResponse = ExceptionsResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details(exchange.getRequest().getURI().toString())
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionsResponse));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Mono<ResponseEntity<ExceptionsResponse>> handleBadCredentialsException(Exception ex, ServerWebExchange exchange) {

        var exceptionsResponse = ExceptionsResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details(exchange.getRequest().getURI().toString())
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionsResponse));
    }

    @ExceptionHandler(ExternalApiRequestException.class)
    public Mono<ResponseEntity<ExceptionsResponse>> handleExternalApiRequestException(Exception ex, ServerWebExchange exchange) {

        var exceptionsResponse = ExceptionsResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details(exchange.getRequest().getURI().toString())
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionsResponse));
    }
}
