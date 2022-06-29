package com.health.application.medhealth.services;

import com.health.application.medhealth.dto.Doctor;
import com.health.application.medhealth.dto.Patient;
import com.health.application.medhealth.exceptions.UnableToProcessException;
import com.health.application.medhealth.repository.DoctorRepository;
import com.health.application.medhealth.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.health.application.medhealth.utils.CustomUtils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        if (id != null && !id.trim().isEmpty()) {
            id = id.trim().toUpperCase();

            if (id.startsWith("PAT")) {
                Optional<Patient> patientDTO = patientRepository.findById(id);
                return patientDTO.orElseThrow(() -> new UsernameNotFoundException("Patient not found"));
            } else if (id.startsWith("DOC")) {
                Optional<Doctor> doctorDTO = doctorRepository.findById(id);
                return doctorDTO.orElseThrow(() -> new UsernameNotFoundException("Doctor not found"));
            }
        }
        throw new UsernameNotFoundException("User Id not found");
    }

    @Override
    public Patient savePatient(Patient patient) {
        isPatientValid(patient);
        if (patient.getId() == null) {
            log.info("Saving new patient {} to the database", patient.getUsername());
            patient.setPassword(passwordEncoder.encode(patient.getPassword()));
            return patientRepository.save(patient);
        }
        throw new UnableToProcessException("Id should not be entered, while registering the patient");
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        isDoctorValid(doctor);
        if (doctor.getId() == null) {
            log.info("Saving new doctor {} to the database", doctor.getUsername());
            doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
            return doctorRepository.save(doctor);
        }
        throw new UnableToProcessException("Id should not be entered, while registering the doctor");
    }

    @Override
    public Object getUser(String id, String email) {
        if (id != null && !id.trim().isEmpty() && email != null && !email.trim().isEmpty()) {
            id = id.trim().toUpperCase();
            if (id.startsWith("PAT")) {
                return patientRepository.findByIdAndEmail(id, email).orElseThrow(() -> new UnableToProcessException("Patient not found"));
            } else if (id.startsWith("DOC")) {
                return doctorRepository.findByIdAndEmail(id, email).orElseThrow(() -> new UnableToProcessException("Doctor not found"));
            }
        }
        throw new UnableToProcessException("Please enter valid userId and email");
    }
}
