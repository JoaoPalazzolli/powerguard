package br.com.inovatech.powerguard.services;

import br.com.inovatech.powerguard.domains.EnergyDomain;
import br.com.inovatech.powerguard.dtos.EnergyDTO;
import br.com.inovatech.powerguard.infra.configs.CacheEnvironmentConfig;
import br.com.inovatech.powerguard.infra.external.proxy.EnergyMonitoringAPI;
import br.com.inovatech.powerguard.infra.security.utils.AuthenticatedUserUtils;
import br.com.inovatech.powerguard.infra.utils.BuildingType;
import br.com.inovatech.powerguard.infra.utils.StringUtil;
import br.com.inovatech.powerguard.repositories.EnergyRepository;
import br.com.inovatech.powerguard.infra.utils.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Service responsável por gerenciar dados de energia, incluindo atualização,
 * cache e recuperação de informações de monitoramento de energia de prédios.
 * Este serviço faz uso de Redis para cache e atualiza periodicamente os dados
 * através de chamadas para APIs de monitoramento de energia.
 */
@Slf4j
@Service
public class EnergyService {

    @Autowired
    private ReactiveRedisTemplate<String, Object> redisTemplate;

    @Autowired
    private EnergyRepository energyRepository;

    @Autowired
    private List<EnergyMonitoringAPI> energyMonitoringAPI;

    @Autowired
    private CacheEnvironmentConfig cacheKeys;

    /**
     * Recupera todos os dados de energia do cache do Redis para o usuário autenticado.
     *
     * @return Mono<ResponseEntity<List<EnergyDTO>>> contendo a lista de dados de energia
     *         ou uma resposta sem conteúdo caso o cache esteja vazio.
     */
    @SuppressWarnings("unchecked")
    public Mono<ResponseEntity<List<EnergyDTO>>> findAll() {
        log.info("Buscando dados de energia");
        return AuthenticatedUserUtils.getUser()
                .flatMap(user -> redisTemplate.opsForValue().get(user.getKeyRequest()))
                .map(result -> {
                    List<EnergyDTO> energyList = (List<EnergyDTO>) result;
                    return ResponseEntity.ok(energyList);
                })
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    /**
     * Método agendado para atualizar os dados de energia no banco de dados e no cache Redis
     * a cada 5 minutos (300000 ms). Para cada prédio monitorado, o serviço atualiza os dados no banco
     * de dados e armazena os novos dados no cache correspondente.
     */
    @Scheduled(fixedRate = 300000)
    private void refreshEnergyData() {
        log.info("Refreshing Energy Data");

        energyMonitoringAPI.forEach(building -> {
            updateEnergyInDB(building)
                    .thenMany(findAllByBuilding(building.getBuildingName()))
                    .collectList()
                    .flatMapMany(energyList -> {
                        var cacheKey = cacheKeys.getCaches();
                        String key = "";

                        if(building.getBuildingName().equals(BuildingType.Building_A.name())){
                            key = cacheKey[0];
                        } else if (building.getBuildingName().equals(BuildingType.Building_C.name())){
                            key = cacheKey[1];
                        } else{
                            key = cacheKey[2];
                        }

                        return redisTemplate.opsForValue()
                                .set(key, energyList)
                                .thenMany(Flux.fromIterable(energyList));
                    })
                    .subscribe();
        });
    }

    /**
     * Recupera todos os dados de energia de um prédio específico.
     *
     * @param building Nome do prédio para o qual os dados de energia devem ser recuperados.
     * @return Flux<EnergyDTO> contendo os dados de energia correspondentes ao prédio.
     */
    private Flux<EnergyDTO> findAllByBuilding(String building) {
        return energyRepository.findByBuilding(building)
                .map(energyDomain -> Mapper.parseObject(energyDomain, EnergyDTO.class));
    }

    /**
     * Atualiza o banco de dados com novos dados de energia de um determinado prédio.
     * Apenas novos dados, que ainda não estão no banco, são salvos.
     *
     * @param building API de monitoramento do prédio a partir da qual os dados serão recuperados.
     * @return Mono<Void> indicando a conclusão da operação de atualização.
     */
    private Mono<Void> updateEnergyInDB(EnergyMonitoringAPI building) {
        log.info("Updating Energy data in Database");

        return energyRepository.findAll()
                .collectList()
                .flatMap(dbEnergy ->
                        building.getEnergyData().filter(buildingEnergy -> {
                                    String buildingName = building.getBuildingName();

                                    buildingEnergy.setBuilding(buildingName);

                                    buildingEnergy.setId(buildingEnergy.getId() +
                                            StringUtil.getAnyCharInString(buildingName, buildingName.length() - 1));

                                    return dbEnergy.stream().noneMatch(db ->
                                            buildingEnergy.getId().equals(db.getId()));
                                }).collectList()
                                .flatMapMany(newEnergiesData ->
                                        Flux.fromIterable(newEnergiesData)
                                                .flatMap(newEnergy ->
                                                        energyRepository.save(Mapper.parseObject(newEnergy, EnergyDomain.class))
                                                                .doOnSuccess(savedEnergyDomain ->
                                                                        log.info("Saved new energy data with ID: {} in building: {}",
                                                                                savedEnergyDomain.getId(), building.getBuildingName())
                                                                )
                                                ).doFinally(x -> Mapper.parseObject(x, EnergyDTO.class))
                                ).then());
    }
}
