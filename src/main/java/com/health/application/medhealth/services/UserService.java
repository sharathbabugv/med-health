package com.health.application.medhealth.services;

import com.health.application.medhealth.dto.Appointment;
import com.health.application.medhealth.dto.Doctor;
import com.health.application.medhealth.dto.DoctorSpecialityDTO;
import com.health.application.medhealth.dto.Patient;

import java.util.List;
import java.util.Map;

public interface UserService {

    Patient savePatient(Patient user);

    Doctor saveDoctor(Doctor doctor);

    Object getUser(String id, String email);

    List<DoctorSpecialityDTO> findDoctorsBySpeciality(String speciality);

    Map<String, String> saveAppointment(Appointment appointment);
}
