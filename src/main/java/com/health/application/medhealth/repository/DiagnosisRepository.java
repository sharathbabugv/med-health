package com.health.application.medhealth.repository;

import com.health.application.medhealth.dto.Diagnosis;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface DiagnosisRepository extends CrudRepository<Diagnosis, Long> {

    List<Diagnosis> findByPatientId(String id);

    List<Diagnosis> findByDoctorId(String id);
}
