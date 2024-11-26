package com.itrail.klinikreact.repositories;

import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;
import com.itrail.klinikreact.models.UserBlocking;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserBlockingRepository extends ReactiveCrudRepository<UserBlocking, Long>{

    @Transactional
    @Modifying
    @Query( "UPDATE user_blocking \n" + 
            "SET status = false, status_block = 2, date_unblock = current_timestamp \n" + 
            "WHERE status = true AND date_plan_unblock <= current_timestamp")
    public Mono<Void> unblockUserBlocking();
    
    @Transactional
    @Modifying
    @Query( "UPDATE user_blocking \n" +
            "SET status_block = 3 \n" +
            "WHERE status = false and status_block = 2" )
    public Mono<Void> unblockUserBlockingStatus();

    @Query("SELECT distinct (ub.user_id) FROM user_blocking ub WHERE ub.status = false and ub.status_block = 2 AND (ub.date_unblock BETWEEN :from and :to )")
    public Flux<Long> getBlockStatus( LocalDateTime from, LocalDateTime to );

}