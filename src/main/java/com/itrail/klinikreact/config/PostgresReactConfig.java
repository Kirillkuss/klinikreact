package com.itrail.klinikreact.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.itrail.klinikreact.repositories")
public class PostgresReactConfig {
    
}
