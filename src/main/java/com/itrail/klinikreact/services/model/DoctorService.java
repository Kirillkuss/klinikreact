package com.itrail.klinikreact.services.model;

import java.util.NoSuchElementException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.itrail.klinikreact.models.Doctor;
import com.itrail.klinikreact.repositories.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorService {
    
    private final DoctorRepository doctorRepository;

    @Cacheable("getLazyDoctors") 
    public Flux<Doctor> getLazyDoctors( int page, int size ){
        long startTime = System.currentTimeMillis();
        Flux<Doctor> reponse = doctorRepository.findAll()
                .skip((page - 1) * size)
                .take(size);
        log.info( "Execution time - getLazyDoctors: " + (System.currentTimeMillis() - startTime) + " ms" );
        return reponse;
    }

    public Mono<Long> getCountDoctors(){
        long startTime = System.currentTimeMillis();
        Mono<Long> response = doctorRepository.count();
        log.info( "Execution time - getCountDoctors: " + (System.currentTimeMillis() - startTime) + " ms" );
        return response;
    }

    public Flux<Doctor> findDoctorsByFIO( String param, int page, int size ){
        long startTime = System.currentTimeMillis();
        Flux<Doctor> response = doctorRepository.findDoctorsByFIO( "%"+ param + "%"  )
                                                .skip((page - 1) * size)
                                                .take(size)
                                                .switchIfEmpty( Flux.error( new NoSuchElementException( "По данному запросу ничего не найдено")));
        //response.subscribe( doctor -> log.info( doctor.toString() ),error -> log.info("ERROR: " + error),() -> log.info("Success"));
        log.info( "Execution time - findDoctorsByFIO: " + (System.currentTimeMillis() - startTime) + " ms" );
        return response;
    }

    public Mono<Doctor> saveDoctor( Doctor doctor ){
        long startTime = System.currentTimeMillis();
        Mono<Doctor> responce = doctorRepository.save( doctor );
        log.info( "Execution time - saveDoctor: " + (System.currentTimeMillis() - startTime) + " ms" );
        return responce;
    }

    public Mono<Doctor> getFindById( Long id ){
        return doctorRepository.findById( id )
            .switchIfEmpty( Mono.error( new NoSuchElementException( "По данному запросу ничего не найдено")));
    }
}
