package com.itrail.klinikreact.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.itrail.klinikreact.models.Document;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DocumentRepository extends ReactiveCrudRepository<Document, Long>{

    @Query("SELECT d.* FROM document d WHERE d.numar = :numar")
    Mono<Document> findDocumentByNumar( String numer );

    @Query("SELECT d.* FROM document d WHERE d.snils = :snils")
    Mono<Document> findDocumentBySnils( String snils );

    @Query("SELECT d.* FROM document d WHERE d.polis = :polis")
    Mono<Document> findDocumentByPolis( String polis );

    @Query("SELECT u.* FROM Document u WHERE u.numar LIKE :word or u.snils LIKE :word or u.polis LIKE :word")
    Flux<Document> findByNSP( String word);
    
}
