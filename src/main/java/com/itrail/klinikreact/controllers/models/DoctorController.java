package com.itrail.klinikreact.controllers.models;

import org.springframework.web.bind.annotation.RestController;
import com.itrail.klinikreact.models.Doctor;
import com.itrail.klinikreact.rest.models.IDoctor;
import com.itrail.klinikreact.services.model.DoctorService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class DoctorController implements IDoctor {

    private final DoctorService doctorService;

    @Override
    public Flux<Doctor> getLazyDoctors( int page, int size ) {
        return doctorService.getLazyDoctors(page, size);
    }

    @Override
    public Mono<Doctor> getDoctorById(Long id) {
        return doctorService.getFindById( id );
    }

    @Override
    public Mono<Long> getCountDoctor() {
        return doctorService.getCountDoctors();
    }

    @Override
    public Mono<Doctor> addDoctor(Doctor doctor) {
        return doctorService.saveDoctor( doctor );
    }

    @Override
    public Flux<Doctor> findDoctorsByFIO(String word, int page, int size) {
        return doctorService.findDoctorsByFIO( word, page, size );
    }
    
}
