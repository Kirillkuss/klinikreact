package com.itrail.klinikreact.redis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.itrail.klinikreact.redis.model.UserSession;

@Repository
public interface UserSessionRepository extends CrudRepository<UserSession, String>{
    
}
