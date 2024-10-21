package br.com.inovatech.powerguard.infra.external.proxy;

import br.com.inovatech.powerguard.dtos.EnergyDTO;
import br.com.inovatech.powerguard.infra.exceptions.ExternalApiRequestException;
import br.com.inovatech.powerguard.infra.external.dto.ApiResponseDTO;
import br.com.inovatech.powerguard.infra.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Componente responsável por lidar com requisições externas à API de monitoramento de energia.
 * Utiliza o WebClient para realizar chamadas HTTP assíncronas e reativas, retornando os dados de energia.
 */
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

    /**
     * Faz uma requisição para obter os dados de energia de um endpoint específico.
     * Inicialmente faz uma requisição sem paginação e, em seguida, caso o resultado
     * contenha múltiplas páginas, faz uma segunda requisição para obter os dados da última página.
     *
     * @param endpoint O endpoint da API externa que será consultado.
     * @return Flux<EnergyDTO> Fluxo contendo os dados de energia obtidos da API.
     */
    public Flux<EnergyDTO> getEnergyData(String endpoint) {
        return makeRequest(endpoint, null)
                .flatMapMany(response -> {

                    if (response == null) {
                        return Flux.empty();
                    }

                    var page = PageUtils.getLastPage(response.getCount());

                    return makeRequest(endpoint, page)
                            .flatMapMany(secondResponse -> Flux.fromIterable(secondResponse.getResults()));
                });
    }

    /**
     * Método auxiliar para realizar a requisição HTTP para a API externa.
     * O método constrói a URI baseada nos parâmetros fornecidos e utiliza o WebClient para realizar
     * a chamada e processar a resposta.
     *
     * @param endpoint O endpoint da API externa.
     * @param page     (Opcional) Número da página a ser requisitada, se a API suportar paginação.
     * @return Mono<ApiResponseDTO> Objeto Mono contendo a resposta da API externa.
     */
    private Mono<ApiResponseDTO> makeRequest(String endpoint, Integer page) {
        return webClient.get()
                .uri(uriBuilder -> {
                    var uri = uriBuilder
                            .scheme(SCHEMA)
                            .host(HOST)
                            .port(PORT)
                            .path(endpoint);

                    if (page != null) {
                        uri.queryParam("page", page);
                    }

                    return uri.build();
                })
                .retrieve()
                .bodyToMono(ApiResponseDTO.class)
                .onErrorMap(WebClientResponseException.class, e -> new ExternalApiRequestException("Failed to connect to the external API"));
    }
}
