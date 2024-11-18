package com.itrail.klinikreact.services;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.itrail.klinikreact.models.RecordPatient;
import com.itrail.klinikreact.repositories.CardPatientRepository;
import com.itrail.klinikreact.repositories.DoctorRepository;
import com.itrail.klinikreact.repositories.RecordPatientRepository;
import com.itrail.klinikreact.response.RecordPatientResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RecordPatientService {
    
    private final RecordPatientRepository recordPatientRepository;
    private final DoctorRepository doctorRepository;
    private final CardPatientRepository cardPatientRepository;

    public Flux<RecordPatientResponse> getLazyRecordPatient( int page, int size ){
        return recordPatientRepository.findAll()
            .skip((page - 1) * size)
            .take(size)
            .flatMap( foundLazyRecordPatient ->{ 
                return doctorRepository.findById( foundLazyRecordPatient.getDoctorId() ).map( doctorFound ->{
                    RecordPatientResponse recordPatientResponse = new RecordPatientResponse();
                                          foundLazyRecordPatient.setDoctorId( null );
                                          recordPatientResponse.setRecordPatient( foundLazyRecordPatient );
                                          recordPatientResponse.setDoctor( doctorFound );
                    return recordPatientResponse;
                });
            });
    }

    public Flux<RecordPatientResponse> getRecordPatientByParam( Long idCardPatient, LocalDateTime from, LocalDateTime to ){
        return recordPatientRepository.findByParam( idCardPatient, from, to )
            .flatMap( foundRecordPatient -> {
                return doctorRepository.findById( foundRecordPatient.getDoctorId() ).map( doctor ->{
                    RecordPatientResponse recordPatientResponse = new RecordPatientResponse();
                                          foundRecordPatient.setDoctorId( null );
                                          recordPatientResponse.setRecordPatient( foundRecordPatient );
                                          recordPatientResponse.setDoctor( doctor );
                    return recordPatientResponse;
                });
            }).switchIfEmpty( Mono.error( new NoSuchElementException( "По данному запросу ничего не найдено!")));
    }

    public Mono<RecordPatientResponse> addRecordPatient( RecordPatient recordPatient ){
        return cardPatientRepository.findById( recordPatient.getCardPatientId())
                .switchIfEmpty( Mono.error( new IllegalArgumentException( "Указан неверный идентификатор карты пациента!")))
                .then( doctorRepository.findById( recordPatient.getDoctorId() )
                    .switchIfEmpty( Mono.error( new IllegalArgumentException(  "Указан неверный идентификатор доктора!"))))
                    .flatMap( foundDoctor -> {
                        if( recordPatient.getDateAppointment().isBefore( recordPatient.getDateRecord() )){
                            return Mono.error( new IllegalArgumentException( "Дата приема не может быть раньше даты записи!") );
                        }
                        return recordPatientRepository.save( recordPatient ).map( saveRecordPatient ->{
                            RecordPatientResponse recordPatientResponse = new RecordPatientResponse();
                                                  recordPatient.setDoctorId( null );
                                                  recordPatientResponse.setRecordPatient( recordPatient );
                                                  recordPatientResponse.setDoctor( foundDoctor );
                            return recordPatientResponse;
                        });
                    });
    }

}
