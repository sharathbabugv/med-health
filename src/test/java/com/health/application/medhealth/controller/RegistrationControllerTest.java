package com.health.application.medhealth.controller;

import com.health.application.medhealth.dto.Doctor;
import com.health.application.medhealth.dto.Patient;
import com.health.application.medhealth.exceptions.UnableToProcessException;
import com.health.application.medhealth.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @MockBean
    private UserService service;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(username = "developer")
    void registerPatient() throws Exception {
        Patient patient = new Patient("PAT103", "test@gmail.com", "password", "test", "M",
                LocalDate.of(1996, 6, 6), "1234567890");

        when(service.savePatient(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/register/patient")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"TEST@gmail.com\", \"password\": \"sharath\", \"name\": \"tEST\", \"gender\": \"M\", \"dateOfBirth\": \"1998-04-21\", \"phoneNumber\": \"9986060550\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(username = "developer")
    void registerDoctor() throws Exception {
        Doctor doctor = new Doctor("DOC101", "test@gmail.com", "password", "test", "F",
                LocalDate.of(1996, 6, 6), "1234567890", "MBBS", "ENT", LocalDate.of(2020, 6, 6), null);

        when(service.saveDoctor(any(Doctor.class))).thenReturn(doctor);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/register/doctor")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"TEST@gmail.com\",\"password\": \"sharath\",\"name\": \"tEST\",\"gender\": \"M\",\"dateOfBirth\": \"1998-04-21\",\"phoneNumber\":\"9986060550\",\"qualification\": \"MBBS\",\"speciality\": \"ENT\",\"workStartDate\": \"2000-10-10\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(username = "developer")
    void registerPatientNotCreated() throws Exception {
        when(service.savePatient(any(Patient.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/register/patient")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"TEST@gmail.com\", \"password\": \"sharath\", \"name\": \"tEST\", \"gender\": \"M\", \"dateOfBirth\": \"1998-04-21\", \"phoneNumber\": \"9986060550\"}"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof UnableToProcessException));
    }

    @Test
    @WithMockUser(username = "developer")
    void registerDoctorNotCreated() throws Exception {
        when(service.saveDoctor(any(Doctor.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/register/doctor")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"TEST@gmail.com\", \"password\": \"sharath\", \"name\": \"tEST\", \"gender\": \"M\", \"dateOfBirth\": \"1998-04-21\", \"phoneNumber\": \"9986060550\"}"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof UnableToProcessException));
    }
}