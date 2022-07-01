package com.health.application.medhealth.services;

import com.health.application.medhealth.dto.*;
import com.health.application.medhealth.exceptions.UnableToProcessException;
import com.health.application.medhealth.repository.*;
import com.health.application.medhealth.utils.AppointmentStatus;
import com.health.application.medhealth.utils.CustomUtils;
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
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final DiagnosisRepository diagnosisRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        if (id != null && !id.trim().isEmpty()) {
            id = id.trim().toUpperCase();

            if (id.startsWith("PAT")) {
                log.info("loadUserByUsername : patient..");
                Optional<Patient> patientDTO = patientRepository.findById(id);
                return patientDTO.orElseThrow(() -> new UsernameNotFoundException("Patient not found"));
            } else if (id.startsWith("DOC")) {
                log.info("loadUserByUsername : doctor..");
                Optional<Doctor> doctorDTO = doctorRepository.findById(id);
                return doctorDTO.orElseThrow(() -> new UsernameNotFoundException("Doctor not found"));
            }
        }
        log.warn("User Id not found");
        throw new UsernameNotFoundException("User Id not found");
    }

    @Override
    public Patient savePatient(Patient patient) {
        isPatientValid(patient);
        if (patient.getId() == null || patient.getId().trim().isEmpty()) {
            if (validateEmail(patient.getEmail()) && patientRepository.findByEmail(patient.getEmail().trim()).isEmpty()) {
                log.info("Saving new patient {} to the database", patient.getUsername());
                patient.setEmail(patient.getEmail().trim());
                patient.setPassword(passwordEncoder.encode(patient.getPassword()));
                return patientRepository.save(patient);
            } else {
                log.error("Email ID already exists in our database or is invalid. Accepted domain is gmail.com. Please check and try again");
                throw new UnableToProcessException("Email ID already exists in our database or is invalid. Accepted domain is gmail.com. Please check and try again");
            }
        }
        log.error("Id should not be entered, while registering the doctor");
        throw new UnableToProcessException("Id should not be entered, while registering the patient");
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        isDoctorValid(doctor);
        if (doctor.getId() == null || doctor.getId().trim().isEmpty()) {
            if (validateEmail(doctor.getEmail()) && doctorRepository.findByEmail(doctor.getEmail().trim()).isEmpty()) {
                log.info("Saving new doctor {} to the database", doctor.getUsername());
                doctor.setEmail(doctor.getEmail().trim());
                doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
                return doctorRepository.save(doctor);
            } else {
                log.error("Email ID already exists in our database or is invalid. Accepted domain is gmail.com. Please check and try again");
                throw new UnableToProcessException("Email ID already exists in our database or is invalid. Accepted domain is gmail.com. Please check and try again");
            }
        }
        log.error("Id should not be entered, while registering the doctor");
        throw new UnableToProcessException("Id should not be entered, while registering the doctor");
    }

    @Override
    public Object getUser(String id, String email) {
        if (isStringNotNull(id) && isStringNotNull(email)) {
            id = id.trim().toUpperCase();
            if (id.startsWith("PAT")) {
                log.info("User is patient..");
                return patientRepository.findByIdAndEmail(id, email).orElseThrow(() -> new UnableToProcessException("Patient not found"));
            } else if (id.startsWith("DOC")) {
                log.info("User is doctor..");
                return doctorRepository.findByIdAndEmail(id, email).orElseThrow(() -> new UnableToProcessException("Doctor not found"));
            }
        }
        log.warn("Please enter valid userId and email");
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
        log.warn("Please enter valid speciality");
        throw new UnableToProcessException("Please enter valid speciality");
    }

    @Override
    public Map<String, String> saveAppointment(Appointment appointment) {
        if (appointment.getId() == null) {
            if (isStringNotNull(appointment.getDoctorId()) && isStringNotNull(appointment.getPatientId())) {
                Patient patient = patientRepository.findById(appointment.getPatientId()).orElseThrow(() -> new UnableToProcessException("Patient Id not found"));
                Doctor doctor = doctorRepository.findById(appointment.getDoctorId()).orElseThrow(() -> new UnableToProcessException("Doctor Id not found"));
                appointment.setStatus(AppointmentStatus.OPENED.name());
                appointmentRepository.save(appointment);
                log.info("Appointment saved..");
                Map<String, String> messageMap = new HashMap<>();
                String message = String.format("Dear %s, Thanks for choosing Med App. You have booked %s for your medical appointment.", patient.getName(), "Dr." + doctor.getName());
                messageMap.put("message", message);
                return messageMap;
            }
            log.warn("Id should not be entered, while registering the patient");
            throw new UnableToProcessException("Please enter valid Patient/Doctor ID");
        }
        log.warn("Id should not be entered, while registering the patient");
        throw new UnableToProcessException("Id should not be entered, while registering the patient");
    }

    @Override
    public IllnessMail sendMail(Illness illness) {
        if (isStringNotNull(illness.getDoctorId()) && isStringNotNull(illness.getPatientId()) && isStringNotNull(illness.getAppointmentId())) {
            Appointment appointment = appointmentRepository.findById(Long.valueOf(illness.getAppointmentId())).orElseThrow(() -> new UnableToProcessException("Appointment Id not found"));
            if (appointment.getDoctorId().equals(illness.getDoctorId()) && appointment.getPatientId().equals(illness.getPatientId())) {
                // update appointment table
                appointmentRepository.updateStatus(AppointmentStatus.ILLNESS_SENT.name(), Long.valueOf(illness.getAppointmentId()));
                log.info("Appointment status updated..");
                Patient patient = patientRepository.findById(illness.getPatientId()).orElseThrow(() -> new UnableToProcessException("Patient Id not found"));
                Doctor doctor = doctorRepository.findById(illness.getDoctorId()).orElseThrow(() -> new UnableToProcessException("Doctor Id not found"));

                // adding required to model class
                IllnessMail illnessMail = new IllnessMail();
                illnessMail.setAppointmentId(illness.getAppointmentId());
                illnessMail.setDoctorEmail(doctor.getEmail());
                illnessMail.setDoctorName(doctor.getName());
                illnessMail.setPatientIllness(illness.getIllness());
                illnessMail.setPatientAge(CustomUtils.getDifferenceBetweenDates(patient.getDateOfBirth()));
                illnessMail.setPatientContact(patient.getPhoneNumber());
                illnessMail.setPatientEmail(patient.getEmail());
                illnessMail.setPatientName(patient.getName());

                return illnessMail;
            }
            log.warn("Please enter valid Patient/Doctor/Appointment ID");
            throw new UnableToProcessException("Patient ID and Doctor ID is not matching for the given Appointment ID");
        }
        log.warn("Please enter valid Patient/Doctor/Appointment ID");
        throw new UnableToProcessException("Please enter valid Patient/Doctor/Appointment ID");
    }

    @Override
    public DiagnosisMail sendPrescription(Diagnosis diagnosis) {
        if (isStringNotNull(diagnosis.getDoctorId()) && isStringNotNull(diagnosis.getPatientId())
                && isStringNotNull(diagnosis.getAppointmentId()) && diagnosis.getPrescription() != null && !diagnosis.getPrescription().isEmpty()) {
            Appointment appointment = appointmentRepository.findById(Long.valueOf(diagnosis.getAppointmentId())).orElseThrow(() -> new UnableToProcessException("Appointment Id not found"));
            if (appointment.getDoctorId().equals(diagnosis.getDoctorId()) && appointment.getPatientId().equals(diagnosis.getPatientId())) {
                log.info("sendPrescription validation success");
                appointmentRepository.updateStatus(AppointmentStatus.PRESCRIPTION_SENT.name(), Long.valueOf(diagnosis.getAppointmentId()));

                diagnosisRepository.save(diagnosis);

                Patient patient = patientRepository.findById(diagnosis.getPatientId()).orElseThrow(() -> new UnableToProcessException("Patient Id not found"));
                Doctor doctor = doctorRepository.findById(diagnosis.getDoctorId()).orElseThrow(() -> new UnableToProcessException("Doctor Id not found"));

                DiagnosisMail diagnosisMail = new DiagnosisMail();
                diagnosisMail.setAppointmentId(diagnosis.getAppointmentId());

                diagnosisMail.setPrescription(diagnosis.getPrescription());
                diagnosisMail.setDoctorId(diagnosis.getDoctorId());
                diagnosisMail.setDoctorName(doctor.getName());
                diagnosisMail.setDoctorEmail(doctor.getEmail());

                diagnosisMail.setPatientId(diagnosis.getPatientId());
                diagnosisMail.setPatientName(patient.getName());
                diagnosisMail.setPatientContact(patient.getPhoneNumber());
                diagnosisMail.setPatientEmail(patient.getEmail());

                return diagnosisMail;
            }
            log.warn("Patient ID and Doctor ID is not matching for the given Appointment ID");
            throw new UnableToProcessException("Patient ID and Doctor ID is not matching for the given Appointment ID");
        }
        log.warn("Please enter all the required details");
        throw new UnableToProcessException("Please enter all the required details");
    }

    @Override
    public List<Diagnosis> findDiagnosisForId(String id, String check) {
        if (isStringNotNull(id)) {
            id = id.trim().toUpperCase();
            List<Diagnosis> diagnosisList = new ArrayList<>();
            if (id.startsWith(check) && "PAT" .equals(check)) {
                diagnosisList.addAll(diagnosisRepository.findByPatientId(id));
            } else if (id.startsWith(check) && "DOC" .equals(check)) {
                diagnosisList.addAll(diagnosisRepository.findByDoctorId(id));
            } else {
                log.warn("Invalid ID. Please try again with valid ID");
                throw new UnableToProcessException("Invalid ID. Please try again with valid ID");
            }
            return diagnosisList;
        }
        log.warn("Please enter valid ID");
        throw new UnableToProcessException("Please enter valid ID");
    }

}

