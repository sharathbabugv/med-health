package com.health.application.medhealth.services;

import com.health.application.medhealth.dto.Doctor;
import com.health.application.medhealth.dto.Patient;

public interface UserService {

    Patient savePatient(Patient user);

    Doctor saveDoctor(Doctor doctor);

    Object getUser(String id, String email);
}
