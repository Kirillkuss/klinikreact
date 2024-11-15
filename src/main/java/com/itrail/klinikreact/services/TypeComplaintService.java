package com.itrail.klinikreact.services;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.itrail.klinikreact.models.TypeComplaint;
import com.itrail.klinikreact.repositories.ComplaintRepository;
import com.itrail.klinikreact.repositories.TypeComplaintRepository;
import com.itrail.klinikreact.response.TypeComplaintResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TypeComplaintService {
    
    private final TypeComplaintRepository typeComplaintRepository;
    private final ComplaintRepository complaintRepository;

    public Flux<TypeComplaint> getLazyTypeComplaint( int page, int size ){
        return typeComplaintRepository.findAll()
            .skip((page - 1) * size)
            .take(size);
    }

    public Mono<TypeComplaintResponse> getTypeComplaintsByIdComplaint( Long idComplaint ) {
        return complaintRepository.findById(idComplaint)
            .switchIfEmpty(Mono.error(new NoSuchElementException("Жалобы с таким ИД не существует")))
            .flatMap(complaint -> {
                return typeComplaintRepository.findTypeComplaintByIdComplaint(idComplaint)
                    .collectList() 
                    .map( typeComplaints -> {
                        TypeComplaintResponse response = new TypeComplaintResponse();
                                              response.setComplaint(complaint); 
                                              response.setTypeComplaints( typeComplaints ) ;
                        return response; 
                    });
            });
    }

    public Mono<TypeComplaint> addTypeComplaint( TypeComplaint typeComplaint ){
        return checkTypeComplaint( typeComplaint )
                .then( typeComplaintRepository.save( typeComplaint ));   
    }

    private Mono<Void> checkTypeComplaint(TypeComplaint typeComplaint) {
        return complaintRepository.findById(typeComplaint.getComplaintId())
            .switchIfEmpty(Mono.error(new NoSuchElementException("Жалобы с таким ИД не существует")))
            .then(typeComplaintRepository.findTypeComplaintByName(typeComplaint.getName())
                .flatMap(foundTypeComplaint -> Mono.error(new IllegalArgumentException("Под жалоба с таким наименованием уже существует")))
                .defaultIfEmpty(Mono.empty()) 
            ).then();
    }





}
