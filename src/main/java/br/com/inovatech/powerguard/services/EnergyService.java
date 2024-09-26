package br.com.inovatech.powerguard.services;

import br.com.inovatech.powerguard.domains.Energy;
import br.com.inovatech.powerguard.dtos.EnergyDTO;
import br.com.inovatech.powerguard.infra.external.proxy.EnergyMonitoringAPI;
import br.com.inovatech.powerguard.repositories.EnergyRepository;
import br.com.inovatech.powerguard.infra.utils.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class EnergyService {

    @Autowired
    private ReactiveRedisTemplate<String, Object> redisTemplate;

    @Autowired
    private EnergyRepository energyRepository;

    @Autowired
    private List<EnergyMonitoringAPI> energyMonitoringAPI;

    @Value("${security.cache_key}")
    private String CACHE_KEY;

    public EnergyService(List<EnergyMonitoringAPI> energyMonitoringAPI) {
        this.energyMonitoringAPI = energyMonitoringAPI;
    }

    public Mono<Object> findAll() {
        return redisTemplate.opsForValue().get(CACHE_KEY);
    }

    @Scheduled(fixedRate = 300000)
    private void refreshEnergyData() {
        log.info("Refreshing Energy Data");

            energyMonitoringAPI.forEach(x -> {
                updateEnergyInDB(x)
                        .thenMany(energyRepository.findAll().map(energy -> Mapper.parseObject(energy, EnergyDTO.class)))
                        .collectList()
                        .flatMapMany(energyList ->
                                redisTemplate.opsForValue()
                                        .set(CACHE_KEY, energyList))
                        .subscribe(result -> log.info("Energy data updated and cached successfully"),
                                error -> log.error("Error during energy data update", error));
            });




    }

    private Mono<Void> updateEnergyInDB(EnergyMonitoringAPI api) {
        log.info("Updating Energy data in Database");

        return energyRepository.findAll()
                .collectList()
                .flatMap(dbEnergy ->
                        api.getEnergyData().filter(apiEnergy ->
                                        dbEnergy.stream().noneMatch(x ->
                                                apiEnergy.getId().equals(x.getId()))).collectList()
                                .flatMapMany(energyList ->
                                        Flux.fromIterable(energyList)
                                                .flatMap(newEnergy ->
                                                        energyRepository.save(Mapper.parseObject(newEnergy, Energy.class))
                                                                .doOnSuccess(savedEnergy ->
                                                                        log.info("Saved new energy data with ID: {} in building: {}", savedEnergy.getId(), api.getBuildingName())
                                                                )
                                                ).doFinally(x -> Mapper.parseObject(x, EnergyDTO.class))
                                ).then());

    }
}