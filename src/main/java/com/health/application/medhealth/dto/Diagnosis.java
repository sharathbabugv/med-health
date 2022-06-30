package com.health.application.medhealth.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "diagnosis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String patientId;
    private String doctorId;
    private String appointmentId;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "diagnosis", cascade = {CascadeType.ALL})
    @JsonManagedReference
    private List<Prescription> prescription;
}
