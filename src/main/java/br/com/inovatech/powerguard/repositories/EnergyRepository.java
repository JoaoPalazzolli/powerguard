package br.com.inovatech.powerguard.repositories;

import br.com.inovatech.powerguard.domains.EnergyDomain;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface EnergyRepository extends ReactiveMongoRepository<EnergyDomain, String> {

    @Query("{ 'building': ?0, 'createdAt': { $gte: ?1, $lt: ?2 } }")
    Flux<EnergyDomain> findByBuildingAndToday(String building, LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query("{ }")
    Flux<EnergyDomain> findAll(Pageable pageable);
}
