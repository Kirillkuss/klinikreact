package com.itrail.klinikreact.services.model;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.itrail.klinikreact.aspect.ExecuteTimeLog;
import com.itrail.klinikreact.models.Drug;
import com.itrail.klinikreact.repositories.DrugRepository;
import com.itrail.klinikreact.repositories.DrugTreatmentRepository;
import com.itrail.klinikreact.response.DrugResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DrugService {
    
    private final DrugRepository drugRepository;
    private final DrugTreatmentRepository drugTreatmentRepository;

    public Flux<Drug> getLazyDrug( int page, int size ){
        return drugRepository.findAll()
            .skip((page - 1) * size)
            .take(size);
    }

    /**
     * Получение списка препаратов с мед. лечением
     * @param idDrugTreatment - ид мед. лечения
     * @return Flux DrugResponse
     */
    @ExecuteTimeLog( operation = "findDrugByIdDrugTreatment")
    public Flux<DrugResponse> findDrugByIdDrugTreatment(Long idDrugTreatment) {
        return drugTreatmentRepository.findById(idDrugTreatment)
            .flatMapMany(dt -> {
                DrugResponse drugResponse = new DrugResponse();
                             drugResponse.setDrugTreatment(dt);
                return drugRepository.findDrugByIdDrugTreatment(idDrugTreatment)
                        .collectList()
                        .map(drugs -> {
                            drugs.stream().map( p -> {
                                p.setDrugTreatmentId( null );
                                return p;
                            }).toList();
                            drugResponse.setDrug(drugs);
                            return drugResponse;
                        })
                        .flux(); 
            })
            .switchIfEmpty(Flux.error(new NoSuchElementException( "По данному запросу ничего не найдено"))); 
    }

    @ExecuteTimeLog( operation = "Drug")
    public Mono<Drug> addDrug( Drug drug ){
        return drugRepository.findDrugByName( drug.getName() )
            .flatMap( foundDrug -> Mono.error( new IllegalArgumentException( "Препарат с такми наименованием уже существует" )))
                .then( drugTreatmentRepository.findById( drug.getDrugTreatmentId() ))
                .switchIfEmpty(Mono.error( new IllegalArgumentException( "Медикаментозное лечение с таким ИД не существует")))
                    .then( drugRepository.save( drug ));
    }



}
