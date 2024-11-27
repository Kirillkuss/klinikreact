package com.itrail.klinikreact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;

//http://localhost:8086/webjars/swagger-ui/index.html
@Slf4j
@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(info = @Info(title = "API for klinikReact", version = "1.0", description = "REACT"))
public class KlinikReactApplication {

	public static void main(String[] args) {
		SpringApplication.run(KlinikReactApplication.class, args);
		log.info( "klinikReact success!!!" );
	}

}
