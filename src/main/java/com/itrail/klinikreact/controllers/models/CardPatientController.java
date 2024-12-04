package com.itrail.klinikreact.controllers.models;

import org.springframework.web.bind.annotation.RestController;
import com.itrail.klinikreact.models.CardPatient;
import com.itrail.klinikreact.repositories.CardPatientRepository;
import com.itrail.klinikreact.request.TypeComplaintRequest;
import com.itrail.klinikreact.response.CardPatientResponse;
import com.itrail.klinikreact.rest.models.ICardPatient;
import com.itrail.klinikreact.services.model.CardPatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CardPatientController implements ICardPatient {

    private final CardPatientService cardPatientService;
    private final CardPatientRepository cardPatientRepository;

    @Override
    public Mono<Long> getCountCardPatient() {
        return cardPatientRepository.count();
    }
    @Override
    public Flux<CardPatientResponse> getLazyCardPatient(int page, int size) {
        return cardPatientService.getLazyCardPatient( page, size );
    }

    @Override
    public Flux<CardPatientResponse> getFindCardPatientByDocOrFIO(String param) {
        return cardPatientService.findCardPatientByDocOrFIO( param );
    }

    @Override
    public Mono<CardPatientResponse> addCardPatient(CardPatient cardPatient) {
        return cardPatientService.addCardPatient( cardPatient );
    }

    @Override
    public Mono<Void> addTypeComplaintToCardPatient( TypeComplaintRequest typeComplaintRequest ) {
        return cardPatientService.addTypeComplaintToCardPatient( typeComplaintRequest );
    }


    
}
