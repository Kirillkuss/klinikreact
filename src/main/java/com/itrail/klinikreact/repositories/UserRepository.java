package com.itrail.klinikreact.repositories;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.itrail.klinikreact.models.User;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long>{

    @Query( "SELECT u.* FROM kl_user u WHERE u.login = :login")
    Mono<User> findUserByLogin( String login );
  
    @Query( "SELECT u.* FROM kl_user u WHERE u.email = :email")
    Mono<User> findUserByEmail( String email );

    @Query("SELECT u.* FROM kl_user u WHERE u.login = :word OR u.email = :word")
    Mono<User> findUserByLoginOrByMail( String word );

    @Transactional
    @Modifying
    @Query("UPDATE public.kl_user set status = 'true' where login = :login")
    Mono<Void> blockUser( String login );
}
