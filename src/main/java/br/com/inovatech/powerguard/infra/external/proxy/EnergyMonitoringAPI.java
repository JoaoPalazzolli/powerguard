package br.com.inovatech.powerguard.infra.external.proxy;

import br.com.inovatech.powerguard.dtos.EnergyDTO;
import br.com.inovatech.powerguard.infra.external.domain.ResponseEnergyMonitoringAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

@Component
public class EnergyMonitoringAPI {

    private static final String API_URL = "http://54.208.51.32:30125/api/by-hour/energy_meter_c/";

    @Autowired
    private RestTemplate restTemplate;

    public Flux<EnergyDTO> getByHour() {
        var response = restTemplate.getForEntity(API_URL, ResponseEnergyMonitoringAPI.class);

        if (response.getBody() == null) {
            return null;
        }

        return Flux.fromIterable(response.getBody().getResults());
    }

}
