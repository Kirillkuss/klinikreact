package com.itrail.klinikreact.controllers.models;

import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import com.itrail.klinikreact.models.CardPatient;
import com.itrail.klinikreact.repositories.CardPatientRepository;
import com.itrail.klinikreact.request.TypeComplaintRequest;
import com.itrail.klinikreact.response.BaseError;
import com.itrail.klinikreact.response.CardPatientResponse;
import com.itrail.klinikreact.rest.models.ICardPatient;
import com.itrail.klinikreact.services.CardPatientService;
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

    @ExceptionHandler(Throwable.class)
    public Flux<BaseError> errBaseResponse( Throwable ex ){
        ex.printStackTrace();
        return Flux.just(  new BaseError( 500, ex.getMessage() ));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public Flux<BaseError> errBaseResponse( NoSuchElementException ex ){
        return Flux.just( new BaseError( 400, ex.getMessage() ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Flux<BaseError> errBaseResponse( IllegalArgumentException ex ){
        return Flux.just( new BaseError( 404, ex.getMessage() ));
    }

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
