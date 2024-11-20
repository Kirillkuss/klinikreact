package com.itrail.klinikreact.services.model;

import org.springframework.stereotype.Service;
import com.itrail.klinikreact.models.Complaint;
import com.itrail.klinikreact.repositories.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    
    private final ComplaintRepository complaintRepository;

    public Flux<Complaint> getLazyComplaints( int page, int size ){
        return complaintRepository.findAll()
            .skip((page - 1) * size)
            .take(size);
    }

    public Mono<Complaint> addComplaint( Complaint complaint ){
        return complaintRepository.findComplaintByName( complaint.getFunctionalImpairment() )
                .flatMap( foundComplaint -> Mono.error( new IllegalArgumentException("Справочник жалоба с таким наименованием уже существует"))).then(
                    complaintRepository.save( complaint ));
    }
}
