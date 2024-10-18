package br.com.inovatech.powerguard.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExternalApiRequestException extends RuntimeException {

    public ExternalApiRequestException(String msg){
        super(msg);
    }
}
