package com.itrail.klinikreact.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table( name = "card_patient")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RecordPatient implements Serializable {
    
    @Id
    @Column( name = "id_record")
    @Schema( name        = "idRecordPatient",
             description = "ИД записи пациента",
             example     = "100",
             required    = true )
    private Long idRecordPatient;

    @Column( name = "date_record")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema( name        = "dateRecord",
             description = "Дата и время записи",
             example     = "2023-01-19T12:00:00.000Z",
             required    = true )
    private LocalDateTime dateRecord;

    @Column( name = "date_appointment")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema( name        = "dateAppointment",
             description = "Дата и время приема",
             example     = "2023-02-01T14:00:00.605Z",
             required    = true )
    private LocalDateTime dateAppointment;

    @Column( name = "number_room")
    @Schema( name        = "numberRoom",
             description = "Номер кабинета",
             example     = "203",
             required    = true )
    private Long numberRoom;

    @Hidden
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id_doctor")
    private Doctor doctor;

    @Hidden
    @Column( name = "card_patient_id")
    @Schema( name        = "cardPatientId",
             description = "ИД карты",
             example     = "1",
             required    = true )
    private Long cardPatientId;
}
