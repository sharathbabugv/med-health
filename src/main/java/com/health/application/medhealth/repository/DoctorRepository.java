package com.health.application.medhealth.repository;

import com.health.application.medhealth.dto.Doctor;
import com.health.application.medhealth.dto.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends CrudRepository<Doctor, String> {
    List<Doctor> findBySpeciality(String speciality);

    Optional<Doctor> findByIdAndEmail(String id, String email);

    Optional<Doctor> findByEmail(String email);
}
