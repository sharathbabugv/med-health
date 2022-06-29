package com.health.application.medhealth.repository;

import com.health.application.medhealth.dto.Appointment;
import com.health.application.medhealth.dto.Doctor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {

}
