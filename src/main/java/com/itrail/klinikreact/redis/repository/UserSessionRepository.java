package com.itrail.klinikreact.redis.repository;

import org.springframework.stereotype.Repository;
import com.itrail.klinikreact.redis.model.UserSession;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface UserSessionRepository extends CrudRepository<UserSession, String>{

}
