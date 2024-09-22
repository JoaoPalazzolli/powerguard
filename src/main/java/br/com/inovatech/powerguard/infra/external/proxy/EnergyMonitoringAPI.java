package br.com.inovatech.powerguard.infra.external.proxy;

import br.com.inovatech.powerguard.dtos.EnergyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EnergyMonitoringAPI {

    @Autowired
    private RestTemplate restTemplate;

}
