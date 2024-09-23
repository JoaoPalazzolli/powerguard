package br.com.inovatech.powerguard.services;

import br.com.inovatech.powerguard.domains.Energy;
import br.com.inovatech.powerguard.dtos.EnergyDTO;
import br.com.inovatech.powerguard.infra.external.proxy.EnergyMonitoringAPI;
import br.com.inovatech.powerguard.repositories.EnergyRepository;
import br.com.inovatech.powerguard.utils.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class EnergyService {

    @Autowired
    private EnergyMonitoringAPI api;

    @Autowired
    private ReactiveRedisTemplate<String, Object> redisTemplate;

    @Autowired
    private EnergyRepository energyRepository;

    @Value("${security.cache_key}")
    private String CACHE_KEY;

    public Mono<Object> findAllByHour() {
        return redisTemplate.opsForValue().get(CACHE_KEY);
    }

    @Scheduled(fixedRate = 36000000)
    private void refreshEnergyData() {
        log.info("Refreshing Energy Data");

        updateEnergyInDB()
                .thenMany(energyRepository.findAll().map(energy -> Mapper.parseObject(energy, EnergyDTO.class)))
                .collectList()
                .flatMapMany(energyList ->
                        redisTemplate.opsForValue()
                                .set(CACHE_KEY, energyList))
                .subscribe(result -> log.info("Energy data updated and cached successfully"),
                        error -> log.error("Error during energy data update", error));
        ;
    }

    private Mono<Void> updateEnergyInDB() {
        log.info("Updating Energy data in Database");

        return api.getByHour()
                .flatMap(apiEnergy ->
                        energyRepository.save(Mapper.parseObject(apiEnergy, Energy.class))
                                .doOnNext(savedEnergy ->
                                        log.info("Saved new or updated energy data with ID: {}", savedEnergy.getId())
                                )
                ).doFinally(x -> Mapper.parseObject(x, EnergyDTO.class))
                .then();
    }
}