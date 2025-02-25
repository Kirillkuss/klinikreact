package com.itrail.klinikreact.controllers.models;

import org.springframework.web.bind.annotation.RestController;
import com.itrail.klinikreact.models.Complaint;
import com.itrail.klinikreact.models.TypeComplaint;
import com.itrail.klinikreact.response.TypeComplaintResponse;
import com.itrail.klinikreact.rest.models.IComplaint;
import com.itrail.klinikreact.services.model.ComplaintService;
import com.itrail.klinikreact.services.model.TypeComplaintService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ComplaintCotroller implements IComplaint {

    private final TypeComplaintService typeComplaintService;
    private final ComplaintService complaintService;

    @Override
    public Flux<Complaint> getLazyComplaints(int page, int size ) {
        return complaintService.getLazyComplaints( page, size );
    }

    @Override
    public Mono<Complaint> getAddComplaint(Complaint complaint) {
        return complaintService.addComplaint( complaint );
    }

    @Override
    public Flux<TypeComplaint> getLazyTypeComplaints(int page, int size ) {
        return typeComplaintService.getLazyTypeComplaint( page, size );
    }

    @Override
    public Mono<TypeComplaint> getAddComplaint(TypeComplaint typeComplaint) {
        return typeComplaintService.addTypeComplaint( typeComplaint );
    }

    @Override
    public Mono<TypeComplaintResponse> getTypeComlaintsByIdComplaint(Long id) {
        return typeComplaintService.getTypeComplaintsByIdComplaint( id );
    }
    
}
