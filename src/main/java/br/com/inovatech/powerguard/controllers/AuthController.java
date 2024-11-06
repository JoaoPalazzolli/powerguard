package br.com.inovatech.powerguard.controllers;

import br.com.inovatech.powerguard.dtos.SigninDTO;
import br.com.inovatech.powerguard.dtos.TokenDTO;
import br.com.inovatech.powerguard.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Authenticates a user and returns a token", tags = { "Auth" }, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = TokenDTO.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
    @PostMapping(value = "/signin")
    public Mono<ResponseEntity<TokenDTO>> signin(@RequestBody SigninDTO signinDTO){
        return authService.signin(signinDTO);
    }

    @Operation(summary = "Refresh a user and returns a new token", tags = { "Auth" }, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = TokenDTO.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
    @PutMapping(value = "/refresh")
    public Mono<ResponseEntity<TokenDTO>> refresh(@RequestHeader("Authorization") String refreshToken){
        return authService.refresh(refreshToken);
    }
}
