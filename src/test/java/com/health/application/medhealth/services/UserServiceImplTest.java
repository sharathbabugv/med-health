package com.health.application.medhealth.services;

import com.health.application.medhealth.dto.Doctor;
import com.health.application.medhealth.dto.Patient;
import com.health.application.medhealth.exceptions.UnableToProcessException;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.print.Doc;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

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
}