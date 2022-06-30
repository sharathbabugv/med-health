package com.health.application.medhealth.repository;

import com.health.application.medhealth.dto.Appointment;
import com.health.application.medhealth.dto.Doctor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface AppointmentRepository extends CrudRepository<Appointment, Long> {

    @Modifying
    @Query("UPDATE Appointment SET status = :status where id = :id")
    void updateStatus(@Param("status") String status, @Param("id") Long id);

}
