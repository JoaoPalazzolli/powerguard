package br.com.inovatech.powerguard.repositories;

import br.com.inovatech.powerguard.domains.UserDomain;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<UserDomain, String> {

    @Query("{ 'username': ?0 }")
    Mono<UserDomain> findByUsername(String username);
}
