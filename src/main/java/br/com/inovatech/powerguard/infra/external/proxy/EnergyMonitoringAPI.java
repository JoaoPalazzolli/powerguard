package br.com.inovatech.powerguard.infra.external.proxy;

import br.com.inovatech.powerguard.dtos.EnergyDTO;
import br.com.inovatech.powerguard.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Component
public class EnergyMonitoringAPI {

    private static final String API_URL = "http://54.208.51.32:30125/api/by-hour/energy_meter_c/";

    @Autowired
    private RestTemplate restTemplate;

    @SuppressWarnings("unchecked")
    public Flux<EnergyDTO> getByHour() {
        var response = restTemplate.getForEntity(API_URL, Map.class).getBody();

        if (response == null) {
            return null;
        }

        var resultList = Mapper.parseObject(response.get("results"), List.class);

        var convetedList = Mapper.parseListObject(resultList, EnergyDTO.class);

        return Flux.fromIterable(convetedList);
    }

}
