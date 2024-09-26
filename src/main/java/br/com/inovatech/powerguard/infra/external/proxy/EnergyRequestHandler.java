package br.com.inovatech.powerguard.infra.external.proxy;

import br.com.inovatech.powerguard.dtos.EnergyDTO;
import br.com.inovatech.powerguard.infra.external.dto.ApiResponseDTO;
import br.com.inovatech.powerguard.infra.utils.PageCalculation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class EnergyRequestHandler {

    @Autowired
    private WebClient webClient;

    @Value("${external.api.host}")
    private String HOST;

    @Value("${external.api.port}")
    private String PORT;

    @Value("${external.api.schema}")
    private String SCHEMA;

    public Flux<EnergyDTO> getEnergyData(String endpoint) {
        return makeRequest(endpoint, null)
                .flatMapMany(response -> {

                    if (response == null){
                        return Flux.empty();
                    }

                    var page = PageCalculation.getLastPage(response.getCount());

                    return makeRequest(endpoint, page)
                            .flatMapMany(secondResponse -> Flux.fromIterable(secondResponse.getResults()));
                });
    }

    private Mono<ApiResponseDTO> makeRequest(String endpoint, Integer page){
        return webClient.get()
                .uri(uriBuilder -> {
                    var uri = uriBuilder
                            .scheme(SCHEMA)
                            .host(HOST)
                            .port(PORT)
                            .path(endpoint);

                    if(page != null){
                        uri.queryParam("page", page);
                    }

                    return uri.build();
                })
                .retrieve()
                .bodyToMono(ApiResponseDTO.class);
    }
}
