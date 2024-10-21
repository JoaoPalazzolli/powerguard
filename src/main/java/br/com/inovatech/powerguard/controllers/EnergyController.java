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
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/energy")
public class EnergyController {

    @Autowired
    private EnergyService energyService;

    @Operation(summary = "Finding all energy data from the last 24 hours", description = "Finding all energy data from the last 24 hours", tags = {"Energy"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EnergyDTO.class)))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    @GetMapping
    public Mono<ResponseEntity<List<EnergyDTO>>> findEnergyDataLast24Hours() {
        return energyService.findEnergyDataLast24Hours();
    }

    @Operation(summary = "Finding all energy data history", description = "Finding all energy data history", tags = {"Energy"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EnergyDTO.class)))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    @GetMapping(value = "/history")
    public Mono<ResponseEntity<List<EnergyDTO>>> findAllEnergyDataHistory(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "30") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "orderBy", defaultValue = "createdAt") String orderBy) {
        return energyService.findAllEnergyDataHistory(page, size, direction, orderBy);
    }

    @Operation(summary = "Finds a Energy Data By ID", description = "Finds a Energy Data By ID", tags = {"Energy"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = EnergyDTO.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<EnergyDTO>> findById(@PathVariable(value = "id") String id) {
        return energyService.findById(id);
    }
}
