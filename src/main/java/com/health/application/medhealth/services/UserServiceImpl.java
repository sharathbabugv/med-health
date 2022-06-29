package com.health.application.medhealth.services;

import com.health.application.medhealth.dto.Appointment;
import com.health.application.medhealth.dto.Doctor;
import com.health.application.medhealth.dto.DoctorSpecialityDTO;
import com.health.application.medhealth.dto.Patient;
import com.health.application.medhealth.exceptions.UnableToProcessException;
import com.health.application.medhealth.repository.AppointmentRepository;
import com.health.application.medhealth.repository.DoctorRepository;
import com.health.application.medhealth.repository.PatientRepository;
import com.health.application.medhealth.utils.AppointmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.health.application.medhealth.utils.CustomUtils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppointmentRepository appointmentRepository;

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
        if (patient.getId() == null || patient.getId().trim().isEmpty()) {
            log.info("Saving new patient {} to the database", patient.getUsername());
            patient.setPassword(passwordEncoder.encode(patient.getPassword()));
            return patientRepository.save(patient);
        }
        throw new UnableToProcessException("Id should not be entered, while registering the patient");
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        isDoctorValid(doctor);
        if (doctor.getId() == null || doctor.getId().trim().isEmpty()) {
            log.info("Saving new doctor {} to the database", doctor.getUsername());
            doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
            return doctorRepository.save(doctor);
        }
        throw new UnableToProcessException("Id should not be entered, while registering the doctor");
    }

    @Override
    public Object getUser(String id, String email) {
        if (isStringNotNull(id) && isStringNotNull(email)) {
            id = id.trim().toUpperCase();
            if (id.startsWith("PAT")) {
                return patientRepository.findByIdAndEmail(id, email).orElseThrow(() -> new UnableToProcessException("Patient not found"));
            } else if (id.startsWith("DOC")) {
                return doctorRepository.findByIdAndEmail(id, email).orElseThrow(() -> new UnableToProcessException("Doctor not found"));
            }
        }
        throw new UnableToProcessException("Please enter valid userId and email");
    }

    @Override
    public List<DoctorSpecialityDTO> findDoctorsBySpeciality(String speciality) {
        if (isStringNotNull(speciality)) {
            List<Doctor> doctors = doctorRepository.findBySpeciality(speciality);
            List<DoctorSpecialityDTO> specialityDTOList = new ArrayList<>();

            if (doctors != null && !doctors.isEmpty()) {
                for (Doctor doctor : doctors) {
                    DoctorSpecialityDTO specialityDTO = new DoctorSpecialityDTO();
                    specialityDTO.setName(doctor.getName());
                    specialityDTO.setId(doctor.getId());
                    specialityDTO.setQualification(doctor.getQualification());
                    specialityDTO.setExperience(doctor.getExperience());
                    specialityDTOList.add(specialityDTO);
                }
            }
            return specialityDTOList;
        }
        throw new UnableToProcessException("Please enter valid speciality");
    }

    @Override
    public Map<String, String> saveAppointment(Appointment appointment) {
        if (isStringNotNull(appointment.getDoctorId()) && isStringNotNull(appointment.getPatientId())) {
            Optional<Patient> patient = patientRepository.findById(appointment.getPatientId());
            if (patient.isPresent()) {
                Optional<Doctor> doctor = doctorRepository.findById(appointment.getDoctorId());
                if (doctor.isPresent()) {
                    appointment.setStatus(AppointmentStatus.PENDING.name());
                    appointmentRepository.save(appointment);
                    Map<String, String> messageMap = new HashMap<>();
                    String message = String.format("Dear %s, Thanks for choosing Med App. You have booked %s for your medical appointment.", patient.get().getName(), "Dr." + doctor.get().getName());
                    messageMap.put("message", message);
                    return messageMap;
                }
                throw new UnableToProcessException("Doctor ID not found");
            }
            throw new UnableToProcessException("Patient ID not found");
        }
        throw new UnableToProcessException("Please enter valid Patient/Doctor ID");
    }

}
