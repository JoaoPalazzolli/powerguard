package br.com.inovatech.powerguard.controllers;

import br.com.inovatech.powerguard.dtos.SigninDTO;
import br.com.inovatech.powerguard.dtos.TokenDTO;
import br.com.inovatech.powerguard.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/signin")
    public Mono<ResponseEntity<TokenDTO>> signin(@RequestBody SigninDTO signinDTO){
        return authService.signin(signinDTO);
    }
}
