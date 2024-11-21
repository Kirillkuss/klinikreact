package com.itrail.klinikreact.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.itrail.klinikreact.models.User;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long>{

    @Query( "SELECT u.* FROM kl_user u WHERE u.login = :login")
    Mono<User> findUserByLogin( String login );
  
    @Query( "SELECT u.* FROM kl_user u WHERE u.email = :email")
    Mono<User> findUserByEmail( String email );

    @Query("SELECT u.* FROM kl_user u WHERE u.login = :word OR u.email = :word")
    Mono<User> findUserByLoginOrByMail( String word );

}
