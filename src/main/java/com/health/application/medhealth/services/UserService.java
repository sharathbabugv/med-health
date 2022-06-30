package com.health.application.medhealth.services;

import com.health.application.medhealth.dto.*;

import java.util.List;
import java.util.Map;

public interface UserService {

    Patient savePatient(Patient user);

    Doctor saveDoctor(Doctor doctor);

    Object getUser(String id, String email);

    List<DoctorSpecialityDTO> findDoctorsBySpeciality(String speciality);

    Map<String, String> saveAppointment(Appointment appointment);

    IllnessMail sendMail(Illness illness);

    DiagnosisMail sendPrescription(Diagnosis diagnosis);

    List<Diagnosis> findDiagnosisForId(String id, String check);
}
