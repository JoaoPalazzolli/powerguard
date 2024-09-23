package br.com.inovatech.powerguard.controllers;

import br.com.inovatech.powerguard.services.EnergyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/energy")
public class EnergyController {

    @Autowired
    private EnergyService energyService;

    @GetMapping
    public Mono<Object> findAllByHour() {
        return energyService.findAllByHour();
    }
}
