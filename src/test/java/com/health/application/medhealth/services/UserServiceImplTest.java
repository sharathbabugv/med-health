package com.health.application.medhealth.services;

import com.health.application.medhealth.dto.*;
import com.health.application.medhealth.exceptions.UnableToProcessException;
import com.health.application.medhealth.repository.AppointmentRepository;
import com.health.application.medhealth.repository.DiagnosisRepository;
import com.health.application.medhealth.repository.DoctorRepository;
import com.health.application.medhealth.repository.PatientRepository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.print.Doc;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DiagnosisRepository diagnosisRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    void test_valid_patient_loadUserByUsername() {
        String id = "PAT101";

        Patient patient = new Patient(id, "test@email.com", "password", "test", "M",
                LocalDate.of(1996, 6, 6), "1234567890");

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        assertEquals("PAT101", userService.loadUserByUsername(id).getUsername());
    }

    @Test
    void test_valid_doctor_loadUserByUsername() {
        String id = "DOC101";

        Doctor doctor = new Doctor(id, "test@gmail.com", "password", "test", "F",
                LocalDate.of(1996, 6, 6), "1234567890", "MBBS", "ENT", LocalDate.of(2020, 6, 6), null);

        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));
        assertEquals("DOC101", userService.loadUserByUsername(id).getUsername());
    }

    @Test
    void test_inValid_emptyId_loadUserByUsername() {
        UsernameNotFoundException thrown = Assertions
                .assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(null), "User Id not found");

        assertEquals("User Id not found", thrown.getMessage());
    }

    @Test
    void test_inValid_patientNotFound_loadUserByUsername() {
        String id = "PAT101";

        when(patientRepository.findById(id)).thenReturn(Optional.empty());
        UsernameNotFoundException thrown = Assertions
                .assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(id), "Patient not found");
        assertEquals("Patient not found", thrown.getMessage());
    }

    @Test
    void test_inValid_doctorNotFound_loadUserByUsername() {
        String id = "DOC101";
        when(doctorRepository.findById(id)).thenReturn(Optional.empty());
        UsernameNotFoundException thrown = Assertions
                .assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(id), "Doctor not found");
        assertEquals("Doctor not found", thrown.getMessage());
    }

    @Test
    void test_valid_savePatient() {
        Patient patient = new Patient(null, "test@email.com", "password", "test", "M",
                LocalDate.of(1996, 6, 6), "1234567890");
        userService.savePatient(patient);
        verify(patientRepository).save(patient);
    }

    @Test
    void test_enteringIdWhileRegistering_savePatient() {
        Patient patient = new Patient("PAT_101", "test@email.com", "password", "test", "M",
                LocalDate.of(1996, 6, 6), "1234567890");
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.savePatient(patient), "Id should not be entered, while registering the patient");

        Assertions.assertEquals("Id should not be entered, while registering the patient", thrown.getMessage());
    }

    @Test
    void test_invalidUsernameLength_savePatient() {
        Patient patient = new Patient(null, "test@email.com", "password", "ab", "M",
                LocalDate.of(1996, 6, 6), "1234567890");
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.savePatient(patient), "Username is either empty or doesn't match the minimum characters of 3");

        Assertions.assertEquals("Username is either empty or doesn't match the minimum characters of 3", thrown.getMessage());
    }

    @Test
    void test_invalidPasswordLength_savePatient() {
        Patient patient = new Patient(null, "test@email.com", "pass", "abcd", "M",
                LocalDate.of(1996, 6, 6), "1234567890");
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.savePatient(patient), "Password is either empty or doesn't match the minimum characters of 6");

        Assertions.assertEquals("Password is either empty or doesn't match the minimum characters of 6", thrown.getMessage());
    }

    @Test
    void test_mandatoryEmailEmpty_savePatient() {
        Patient patient = new Patient(null, null, "password", "abcd", "M",
                LocalDate.of(1996, 6, 6), "1234567890");
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.savePatient(patient), "Please enter all the required fields to register");

        Assertions.assertEquals("Please enter all the required fields to register", thrown.getMessage());
    }

    @Test
    void test_mandatoryDOBEmpty_savePatient() {
        Patient patient = new Patient(null, "test@email.com", "password", "abcd", "M",
                null, "1234567890");
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.savePatient(patient), "Please enter all the required fields to register");

        Assertions.assertEquals("Please enter all the required fields to register", thrown.getMessage());
    }

    @Test
    void test_valid_saveDoctor() {
        Doctor doctor = new Doctor(null, "test@gmail.com", "password", "test", "F",
                LocalDate.of(1996, 6, 6), "1234567890", "MBBS", "ENT", LocalDate.of(2020, 6, 6), null);
        userService.saveDoctor(doctor);
        verify(doctorRepository).save(doctor);
    }

    @Test
    void test_enteringIdWhileRegistering_saveDoctor() {
        Doctor doctor = new Doctor("DOC101", "test@gmail.com", "password", "test", "F",
                LocalDate.of(1996, 6, 6), "1234567890", "MBBS", "ENT", LocalDate.of(2020, 6, 6), null);
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.saveDoctor(doctor), "Id should not be entered, while registering the doctor");

        Assertions.assertEquals("Id should not be entered, while registering the doctor", thrown.getMessage());
    }

    @Test
    void test_invalidUsernameLength_saveDoctor() {
        Doctor doctor = new Doctor("DOC101", "test@gmail.com", "password", "AB", "F",
                LocalDate.of(1996, 6, 6), "1234567890", "MBBS", "ENT", LocalDate.of(2020, 6, 6), null);
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.saveDoctor(doctor), "Username is either empty or doesn't match the minimum characters of 3");

        Assertions.assertEquals("Username is either empty or doesn't match the minimum characters of 3", thrown.getMessage());
    }

    @Test
    void test_invalidPasswordLength_saveDoctor() {
        Doctor doctor = new Doctor("DOC101", "test@gmail.com", "PASS", "test", "F",
                LocalDate.of(1996, 6, 6), "1234567890", "MBBS", "ENT", LocalDate.of(2020, 6, 6), null);
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.saveDoctor(doctor), "Password is either empty or doesn't match the minimum characters of 6");

        Assertions.assertEquals("Password is either empty or doesn't match the minimum characters of 6", thrown.getMessage());
    }

    @Test
    void test_mandatoryEmailEmpty_saveDoctor() {
        Doctor doctor = new Doctor("DOC101", null, "PASSWORD", "test", "F",
                LocalDate.of(1996, 6, 6), "1234567890", "MBBS", "ENT", LocalDate.of(2020, 6, 6), null);
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.saveDoctor(doctor), "Please enter all the required fields to register");

        Assertions.assertEquals("Please enter all the required fields to register", thrown.getMessage());
    }

    @Test
    void test_mandatoryDOBEmpty_saveDoctor() {
        Doctor doctor = new Doctor("DOC101", "test@email.com", "PASSWORD", "test", "F",
                null, "1234567890", "MBBS", "ENT", LocalDate.of(2020, 6, 6), null);
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.saveDoctor(doctor), "Please enter all the required fields to register");

        Assertions.assertEquals("Please enter all the required fields to register", thrown.getMessage());
    }

    @Test
    void test_doctor_getUser() {
        String id = "DOC101";
        String email = "test@email.com";

        Doctor doctor = new Doctor(id, email, "PASSWORD", "test", "F",
                LocalDate.of(1996, 6, 6), "1234567890", "MBBS", "ENT", LocalDate.of(2020, 6, 6), null);
        when(doctorRepository.findByIdAndEmail(id, email)).thenReturn(Optional.of(doctor));

        Doctor doc = (Doctor) userService.getUser(id, email);
        assertEquals(id, doc.getId());
    }

    @Test
    void test_patient_getUser() {
        String id = "PAT101";
        String email = "test@email.com";
        Patient patient = new Patient(id, email, "password", "test", "M",
                LocalDate.of(1996, 6, 6), "1234567890");
        when(patientRepository.findByIdAndEmail(id, email)).thenReturn(Optional.of(patient));

        Patient patient1 = (Patient) userService.getUser(id, email);
        assertEquals(id, patient1.getId());
    }

    @Test
    void test_invalid_idAndEmailNull_getUser() {
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.getUser(null, null), "Please enter valid userId and email");

        Assertions.assertEquals("Please enter valid userId and email", thrown.getMessage());
    }

    @Test
    void test__invalid_patientNotFound_getUser() {
        String id = "PAT101";
        String email = "test@email.com";
        when(patientRepository.findByIdAndEmail(id, email)).thenReturn(Optional.empty());

        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.getUser(id, email), "Patient not found");

        Assertions.assertEquals("Patient not found", thrown.getMessage());
    }

    @Test
    void test__invalid_doctorNotFound_getUser() {
        String id = "DOC101";
        String email = "test@email.com";
        when(doctorRepository.findByIdAndEmail(id, email)).thenReturn(Optional.empty());

        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.getUser(id, email), "Doctor not found");

        Assertions.assertEquals("Doctor not found", thrown.getMessage());
    }

    @Test
    void test_findDoctorsBySpeciality() {
        Doctor doctor = new Doctor("id", "email", "PASSWORD", "test", "F",
                LocalDate.of(1996, 6, 6), "1234567890", "MBBS", "ENT", LocalDate.of(2020, 6, 6), null);

        when(doctorRepository.findBySpeciality("ENT")).thenReturn(Collections.singletonList(doctor));
        assertEquals(1, userService.findDoctorsBySpeciality("ENT").size());
    }

    @Test
    void test_invalid_findDoctorsBySpeciality() {
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.findDoctorsBySpeciality(""), "Please enter valid speciality");

        Assertions.assertEquals("Please enter valid speciality", thrown.getMessage());
    }

    @Test
    void test_saveAppointment() {
        Appointment appointment = new Appointment(null, "PAT101", "DOC101", null);
        Patient patient = new Patient();
        Doctor doctor = new Doctor();

        when(patientRepository.findById("PAT101")).thenReturn(Optional.of(patient));
        when(doctorRepository.findById("DOC101")).thenReturn(Optional.of(doctor));

        userService.saveAppointment(appointment);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void test_invalid_idEntered_saveAppointment() {
        Appointment appointment = new Appointment(1L, "PAT101", "DOC101", null);
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.saveAppointment(appointment), "Id should not be entered, while registering the patient");

        Assertions.assertEquals("Id should not be entered, while registering the patient", thrown.getMessage());
    }

    @Test
    void test_invalid_patientIdNull_saveAppointment() {
        Appointment appointment = new Appointment(null, null, "DOC101", null);
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.saveAppointment(appointment), "Please enter valid Patient/Doctor ID");

        Assertions.assertEquals("Please enter valid Patient/Doctor ID", thrown.getMessage());
    }

    @Test
    void test_invalid_doctorIdNull_saveAppointment() {
        Appointment appointment = new Appointment(null, "PAT101", null, null);
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.saveAppointment(appointment), "Please enter valid Patient/Doctor ID");

        Assertions.assertEquals("Please enter valid Patient/Doctor ID", thrown.getMessage());
    }

    @Test
    void test_sendMail() {
        Appointment appointment = new Appointment(1L, "PAT101", "DOC101", null);
        Patient patient = new Patient("PAT101", "test@email.com", "password", "test-user", "M",
                LocalDate.of(1996, 6, 6), "1234567890");
        Doctor doctor = new Doctor("DOC101", "test@gmail.com", "password", "test", "F",
                LocalDate.of(1996, 6, 6), "1234567890", "MBBS", "ENT", LocalDate.of(2020, 6, 6), null);

        when(patientRepository.findById("PAT101")).thenReturn(Optional.of(patient));
        when(doctorRepository.findById("DOC101")).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        Illness illness = mock(Illness.class);
        when(illness.getDoctorId()).thenReturn("DOC101");
        when(illness.getPatientId()).thenReturn("PAT101");
        when(illness.getAppointmentId()).thenReturn("1");

        IllnessMail illnessMail = userService.sendMail(illness);
        verify(appointmentRepository).updateStatus(anyString(), anyLong());
        assertEquals("test", illnessMail.getDoctorName());
        assertEquals("test-user", illnessMail.getPatientName());
        assertEquals("1234567890", illnessMail.getPatientContact());
    }

    @Test
    void test_invalid_PatientIdNotFound_sendMail() {
        Illness illness = new Illness();
        illness.setDoctorId("DOC101");
        illness.setPatientId("PAT101");
        illness.setAppointmentId("1");

        Appointment appointment = new Appointment(1L, "PAT101", "DOC101", null);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.sendMail(illness), "Patient Id not found");

        Assertions.assertEquals("Patient Id not found", thrown.getMessage());
    }

    @Test
    void test_invalid_DoctorIdNotFound_sendMail() {
        Illness illness = new Illness();
        illness.setDoctorId("DOC101");
        illness.setPatientId("PAT101");
        illness.setAppointmentId("1");

        Appointment appointment = new Appointment(1L, "PAT101", "DOC101", null);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        Patient patient = new Patient("PAT101", "test@email.com", "password", "test-user", "M",
                LocalDate.of(1996, 6, 6), "1234567890");
        when(patientRepository.findById("PAT101")).thenReturn(Optional.of(patient));

        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.sendMail(illness), "Doctor Id not found");

        Assertions.assertEquals("Doctor Id not found", thrown.getMessage());
    }

    @Test
    void test_invalid_AppointmentNotFound_sendMail() {
        Illness illness = new Illness();
        illness.setDoctorId("DOC101");
        illness.setPatientId("PAT101");
        illness.setAppointmentId("1");

        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.sendMail(illness), "Appointment Id not found");

        Assertions.assertEquals("Appointment Id not found", thrown.getMessage());
    }

    @Test
    void test_invalid_PatientIdMismatch_sendMail() {
        Illness mockIllness = new Illness();
        mockIllness.setDoctorId("DOC101");
        mockIllness.setPatientId("PAT102");
        mockIllness.setAppointmentId("1");

        Appointment appointment = new Appointment(1L, "PAT101", "DOC101", null);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.sendMail(mockIllness), "Patient ID and Doctor ID is not matching for the given Appointment ID");

        Assertions.assertEquals("Patient ID and Doctor ID is not matching for the given Appointment ID", thrown.getMessage());
    }

    @Test
    void test_invalid_DoctorIdMismatch_sendMail() {
        Illness mockIllness = new Illness();
        mockIllness.setDoctorId("101");
        mockIllness.setPatientId("PAT101");
        mockIllness.setAppointmentId("1");

        Appointment appointment = new Appointment(1L, "PAT101", "DOC101", null);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.sendMail(mockIllness), "Patient ID and Doctor ID is not matching for the given Appointment ID");

        Assertions.assertEquals("Patient ID and Doctor ID is not matching for the given Appointment ID", thrown.getMessage());
    }

    @Test
    void test_invalid_AppointmentIdNull_sendMail() {
        Illness mockIllness = new Illness();
        mockIllness.setDoctorId("101");
        mockIllness.setPatientId("PAT101");
        mockIllness.setAppointmentId(null);

        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.sendMail(mockIllness), "Please enter valid Patient/Doctor/Appointment ID");

        Assertions.assertEquals("Please enter valid Patient/Doctor/Appointment ID", thrown.getMessage());
    }

    @Test
    void test_sendPrescription() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDoctorId("DOC101");
        diagnosis.setAppointmentId("1");
        diagnosis.setPatientId("PAT101");
        diagnosis.setPrescription(List.of(mock(Prescription.class)));

        Patient patient = new Patient("PAT101", "test@email.com", "password", "test-user", "M",
                LocalDate.of(1996, 6, 6), "1234567890");
        when(patientRepository.findById("PAT101")).thenReturn(Optional.of(patient));

        Doctor doctor = new Doctor("DOC101", "test@gmail.com", "password", "test", "F",
                LocalDate.of(1996, 6, 6), "1234567890", "MBBS", "ENT", LocalDate.of(2020, 6, 6), null);
        when(doctorRepository.findById("DOC101")).thenReturn(Optional.of(doctor));

        Appointment appointment = new Appointment(1L, "PAT101", "DOC101", null);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        DiagnosisMail diagnosisMail = userService.sendPrescription(diagnosis);
        verify(diagnosisRepository).save(diagnosis);
        assertEquals("1", diagnosisMail.getAppointmentId());
        assertEquals("test", diagnosisMail.getDoctorName());
        assertEquals("PAT101", diagnosisMail.getPatientId());
    }

    @Test
    void test_doctorIdNull_sendPrescription() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDoctorId(null);
        diagnosis.setPatientId("PAT101");
        diagnosis.setAppointmentId("1");
        diagnosis.setPrescription(List.of(mock(Prescription.class)));

        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.sendPrescription(diagnosis), "Please enter all the required details");

        Assertions.assertEquals("Please enter all the required details", thrown.getMessage());
    }

    @Test
    void test_patientIdNull_sendPrescription() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setPatientId(null);
        diagnosis.setDoctorId("DOC101");
        diagnosis.setAppointmentId("1");
        diagnosis.setPrescription(List.of(mock(Prescription.class)));

        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.sendPrescription(diagnosis), "Please enter all the required details");

        Assertions.assertEquals("Please enter all the required details", thrown.getMessage());
    }

    @Test
    void test_appointmentIdNull_sendPrescription() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setPatientId("PAT101");
        diagnosis.setDoctorId("DOC101");
        diagnosis.setAppointmentId("");
        diagnosis.setPrescription(List.of(mock(Prescription.class)));

        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.sendPrescription(diagnosis), "Please enter all the required details");

        Assertions.assertEquals("Please enter all the required details", thrown.getMessage());
    }

    @Test
    void test_prescriptionListNull_sendPrescription() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setPatientId("PAT101");
        diagnosis.setDoctorId("DOC101");
        diagnosis.setAppointmentId("1");
        diagnosis.setPrescription(null);

        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.sendPrescription(diagnosis), "Please enter all the required details");

        Assertions.assertEquals("Please enter all the required details", thrown.getMessage());
    }

    @Test
    void test_invalidPatientId_sendPrescription() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setPatientId("PAT101");
        diagnosis.setDoctorId("DOC101");
        diagnosis.setAppointmentId("1");
        diagnosis.setPrescription(List.of(mock(Prescription.class)));

        Appointment appointment = new Appointment(1L, "PAT102", "DOC101", null);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.sendPrescription(diagnosis), "Patient ID and Doctor ID is not matching for the given Appointment ID");

        Assertions.assertEquals("Patient ID and Doctor ID is not matching for the given Appointment ID", thrown.getMessage());
    }

    @Test
    void test_invalidDoctorId_sendPrescription() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setPatientId("PAT101");
        diagnosis.setDoctorId("DOC101");
        diagnosis.setAppointmentId("1");
        diagnosis.setPrescription(List.of(mock(Prescription.class)));

        Appointment appointment = new Appointment(1L, "PAT101", "DOC103", null);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.sendPrescription(diagnosis), "Patient ID and Doctor ID is not matching for the given Appointment ID");

        Assertions.assertEquals("Patient ID and Doctor ID is not matching for the given Appointment ID", thrown.getMessage());
    }

    @Test
    void test_appointmentIdNotFound_sendPrescription() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setPatientId("PAT101");
        diagnosis.setDoctorId("DOC101");
        diagnosis.setAppointmentId("1");
        diagnosis.setPrescription(List.of(mock(Prescription.class)));
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.sendPrescription(diagnosis), "Appointment Id not found");

        Assertions.assertEquals("Appointment Id not found", thrown.getMessage());
    }

    @Test
    void test_patientIdNotFound_sendPrescription() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDoctorId("DOC101");
        diagnosis.setAppointmentId("1");
        diagnosis.setPatientId("PAT101");
        diagnosis.setPrescription(List.of(mock(Prescription.class)));

        Appointment appointment = new Appointment(1L, "PAT101", "DOC101", null);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        when(patientRepository.findById("PAT101")).thenReturn(Optional.empty());

        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.sendPrescription(diagnosis), "Patient Id not found");
        Assertions.assertEquals("Patient Id not found", thrown.getMessage());
    }

    @Test
    void test_doctorIdNotFound_sendPrescription() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDoctorId("DOC101");
        diagnosis.setAppointmentId("1");
        diagnosis.setPatientId("PAT101");
        diagnosis.setPrescription(List.of(mock(Prescription.class)));

        Appointment appointment = new Appointment(1L, "PAT101", "DOC101", null);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        Patient patient = new Patient("PAT101", "test@email.com", "password", "test-user", "M",
                LocalDate.of(1996, 6, 6), "1234567890");
        when(patientRepository.findById("PAT101")).thenReturn(Optional.of(patient));

        when(doctorRepository.findById("DOC101")).thenReturn(Optional.empty());

        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.sendPrescription(diagnosis), "Doctor Id not found");
        Assertions.assertEquals("Doctor Id not found", thrown.getMessage());
    }

    @Test
    void test_doctor_findDiagnosisForId() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDoctorId("DOC101");
        diagnosis.setAppointmentId("1");
        diagnosis.setPatientId("PAT101");
        diagnosis.setPrescription(List.of(mock(Prescription.class)));

        when(diagnosisRepository.findByDoctorId("DOC101")).thenReturn(Collections.singletonList(diagnosis));

        List<Diagnosis> diagnosisList = userService.findDiagnosisForId("DOC101", "DOC");
        assertEquals(1, diagnosisList.size());
    }

    @Test
    void test_patient_findDiagnosisForId() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDoctorId("DOC101");
        diagnosis.setAppointmentId("1");
        diagnosis.setPatientId("PAT101");
        diagnosis.setPrescription(List.of(mock(Prescription.class)));

        when(diagnosisRepository.findByPatientId("PAT101")).thenReturn(Collections.singletonList(diagnosis));

        List<Diagnosis> diagnosisList = userService.findDiagnosisForId("PAT101", "PAT");
        assertEquals(1, diagnosisList.size());
    }

    @Test
    void test_patientMismatchId_findDiagnosisForId() {
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.findDiagnosisForId("PAT101", "DOC"), "Invalid ID. Please try again with valid ID");
        Assertions.assertEquals("Invalid ID. Please try again with valid ID", thrown.getMessage());
    }

    @Test
    void test_doctorMismatchId_findDiagnosisForId() {
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.findDiagnosisForId("DOC101", "PAT"), "Invalid ID. Please try again with valid ID");
        Assertions.assertEquals("Invalid ID. Please try again with valid ID", thrown.getMessage());
    }

    @Test
    void test_invalidId_findDiagnosisForId() {
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.findDiagnosisForId("", "PAT"), "Please enter valid ID");
        Assertions.assertEquals("Please enter valid ID", thrown.getMessage());
    }
}