package br.com.inovatech.powerguard.repositories;

import br.com.inovatech.powerguard.domains.Energy;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface EnergyRepository extends ReactiveMongoRepository<Energy, Integer> {
}
