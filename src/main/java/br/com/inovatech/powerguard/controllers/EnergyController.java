package br.com.inovatech.powerguard.controllers;

import br.com.inovatech.powerguard.dtos.EnergyDTO;
import br.com.inovatech.powerguard.services.EnergyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/energy")
public class EnergyController {

    @Autowired
    private EnergyService energyService;

    @Operation(summary = "Finding the last building energy data", description = "Finding the last building energy data", tags = { "Energy" }, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EnergyDTO.class)))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    @GetMapping
    public Mono<ResponseEntity<List<EnergyDTO>>> findAll() {
        return energyService.findAll();
    }

    @Operation(summary = "Finding all energy data history", description = "Finding all energy data history", tags = { "Energy" }, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EnergyDTO.class)))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    @GetMapping(value = "/history")
    public Mono<ResponseEntity<List<EnergyDTO>>> findAllHistory() {
        return energyService.findAllHistory();
    }
}
