package com.itrail.klinikreact.repositories;

import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;
import com.itrail.klinikreact.models.UserBlocking;
import reactor.core.publisher.Flux;

public interface UserBlockingRepository extends ReactiveCrudRepository<UserBlocking, Long>{

        @Transactional
    @Modifying
    @Query( "UPDATE UserBlocking ub \n" 
            + "SET ub.status = false, ub.statusBlock = 2, ub.dateUnblock = current_timestamp \n"
            + "WHERE ub.status = true AND b.datePlanUnblock <= current_timestamp")
    public void unblockUserBlocking();
    
    @Transactional
    @Modifying
    @Query( "UPDATE UserBlocking ub \n" +
            "SET ub.statusBlock = 3 \n" +
            "WHERE ub.status = false and ub.statusBlock = 2" )
    public void unblockUserBlockingStatus();

    @Query("SELECT distinct( ub.user.idUser) FROM UserBlocking ub WHERE ub.status = false and statusBlock = 2 AND (ub.dateUnblock BETWEEN :from and :to )")
    public Flux<Long> getBlockStatus( LocalDateTime from, LocalDateTime to );
}