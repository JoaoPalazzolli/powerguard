package br.com.inovatech.powerguard.infra.external.proxy;

import br.com.inovatech.powerguard.dtos.EnergyDTO;
import reactor.core.publisher.Flux;

public interface EnergyMonitoringAPI {

    Flux<EnergyDTO> getEnergyData();

    String getBuildingName();

}
