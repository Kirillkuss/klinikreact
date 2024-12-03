package com.itrail.klinikreact.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.itrail.klinikreact.models.UserBlocking;

public interface UserBlockingRepository extends ReactiveCrudRepository<UserBlocking, Long>{

}