package br.com.inovatech.powerguard.repositories;

import br.com.inovatech.powerguard.domains.EnergyDomain;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface EnergyRepository extends ReactiveMongoRepository<EnergyDomain, Integer> {

    @Query("{ 'building': ?0 }")
    Flux<EnergyDomain> findByBuilding(String building);
}
