package com.itrail.klinikreact.controllers.models;

import org.springframework.web.bind.annotation.RestController;
import com.itrail.klinikreact.models.Drug;
import com.itrail.klinikreact.models.DrugTreatment;
import com.itrail.klinikreact.response.DrugResponse;
import com.itrail.klinikreact.rest.models.IDrugTreatment;
import com.itrail.klinikreact.services.model.DrugService;
import com.itrail.klinikreact.services.model.DrugTreatmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DrugTreatmentController implements IDrugTreatment{

    private final DrugTreatmentService drugTreatmentService;
    private final DrugService drugService;

    @Override
    public Flux<DrugTreatment> getLazyDrugTreatment(int page, int size) {
        return drugTreatmentService.getLazyDrugTreatment( page, size );
    }

    @Override
    public Flux<Drug> getLayDrug(int page, int size) {
        return drugService.getLazyDrug( page, size );
    }

    @Override
    public Flux<DrugResponse> getListDrugByIdDrugTreatment(Long id) {
        return drugService.findDrugByIdDrugTreatment( id );
    }

    @Override
    public Mono<DrugTreatment> addDrugTreatment(DrugTreatment drugTreatment) {
        return drugTreatmentService.addDrugTreatment( drugTreatment );
    }

    @Override
    public Mono<Drug> addDrug(Drug drug) {
        return drugService.addDrug( drug );
    }
    
}
