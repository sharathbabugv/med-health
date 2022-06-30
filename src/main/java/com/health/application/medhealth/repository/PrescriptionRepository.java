package com.health.application.medhealth.repository;

import com.health.application.medhealth.dto.Prescription;
import org.springframework.data.repository.CrudRepository;

public interface PrescriptionRepository extends CrudRepository<Prescription, Long> {

}
