package br.com.inovatech.powerguard.infra.external.proxy;

import br.com.inovatech.powerguard.dtos.EnergyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class MonitoringBuildingC implements EnergyMonitoringAPI {

    @Value("${external.api.endpoints.building_c}")
    private String ENDPOINT;

    @Autowired
    private EnergyRequestHandler energyRequestHandler;

    @Override
    public Flux<EnergyDTO> getEnergyData() {
        return energyRequestHandler.getEnergyData(this.ENDPOINT);
    }

    @Override
    public String getBuildingName() {
        return "Building C";
    }
}
