package br.com.inovatech.powerguard.infra.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ExceptionsResponse {

    private String message;
    private LocalDateTime timestamp;
    private String details;
}
