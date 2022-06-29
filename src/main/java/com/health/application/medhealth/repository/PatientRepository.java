package com.health.application.medhealth.repository;

import com.health.application.medhealth.dto.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PatientRepository extends CrudRepository<Patient, String> {

    Optional<Patient> findByIdAndEmail(String id, String email);
}
