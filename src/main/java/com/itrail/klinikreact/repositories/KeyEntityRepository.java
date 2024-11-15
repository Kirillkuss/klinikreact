package com.itrail.klinikreact.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.itrail.klinikreact.models.KeyEntity;

import reactor.core.publisher.Mono;

public interface KeyEntityRepository extends ReactiveCrudRepository<KeyEntity, Long>{

    @Query("SELECT k FROM key_entity k WHERE k.alice = :alice")
    Mono<KeyEntity> findKeyEntityByAlice( String alice );
}
