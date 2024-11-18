package com.itrail.klinikreact.services;

import org.springframework.stereotype.Service;

import com.itrail.klinikreact.aspect.ExecuteTimeLog;
import com.itrail.klinikreact.models.DrugTreatment;
import com.itrail.klinikreact.repositories.DrugTreatmentRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DrugTreatmentService {
    
    private final DrugTreatmentRepository drugTreatmentRepository;

    @ExecuteTimeLog( operation = "getLazyDrugTreatment")
    public Flux<DrugTreatment> getLazyDrugTreatment( int page, int size ){
        return drugTreatmentRepository.findAll()
            .skip((page - 1) * size)
            .take(size);
    }

    @ExecuteTimeLog( operation = "getLazyDrugTreatment")
    public Mono<DrugTreatment> addDrugTreatment( DrugTreatment drugTreatment ){
        return drugTreatmentRepository.findDrugTreatmentByName( drugTreatment.getName() )
            .flatMap( foundDrugTreatment -> Mono.error( new IllegalArgumentException(  "Медикаментозное лечение с таким наименование уже существует")))
                .then( drugTreatmentRepository.save( drugTreatment ));
    }

}
