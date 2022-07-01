package com.health.application.medhealth.controller;

import com.health.application.medhealth.dto.*;
import com.health.application.medhealth.services.MessagingService;
import com.health.application.medhealth.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoctorController.class)
class DoctorControllerTest {

    @MockBean
    private UserService service;

    @MockBean
    private MessagingService<DiagnosisMail> diagnosisMailMessagingService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "developer", authorities = {"ROLE_DOCTOR"})
    void getUserDetails() throws Exception {
        Doctor doctor = new Doctor("DOC103", "test@gmail.com", "password", "test", "F",
                LocalDate.of(1996, 6, 6), "1234567890", "MBBS", "ENT", LocalDate.of(2020, 6, 6), null);

        when(service.getUser(any(), any())).thenReturn(doctor);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/doctor/list")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": \"DOC103\",\"email\": \"test@gmail.com\"}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "developer", authorities = {"ROLE_DOCTOR"})
    void sendDiagnosis() throws Exception {
        DiagnosisMail diagnosisMail = new DiagnosisMail();
        diagnosisMail.setDoctorName("Dr.Jack");
        diagnosisMail.setDoctorId("DOC101");
        diagnosisMail.setPatientName("Sharath");
        diagnosisMail.setPatientId("PAT101");

        when(service.sendPrescription(any(Diagnosis.class))).thenReturn(diagnosisMail);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/doctor/send-diagnosis")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientId\": \"PAT103\",\"doctorId\": \"DOC101\",\"appointmentId\": \"3\",\"prescription\": [{\"name\": \"Paracetemol\",\"morning\": true,\"afternoon\": false,\"night\": true,\"numberOfDays\": \"5 days\"}]}"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "developer", authorities = {"ROLE_DOCTOR"})
    void getDoctorDiagnosisList() throws Exception {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDoctorId("DOC101");
        diagnosis.setAppointmentId("1");
        diagnosis.setPatientId("PAT101");

        Prescription prescription = new Prescription();
        prescription.setId(1L);
        prescription.setDiagnosis(diagnosis);
        prescription.setName("Name");
        prescription.setAfternoon(true);
        prescription.setMorning(false);
        prescription.setNight(false);
        prescription.setNumberOfDays("10 days");

        diagnosis.setPrescription(List.of(prescription));

        when(service.findDiagnosisForId("DOC101", "DOC")).thenReturn(List.of(diagnosis));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/doctor/diagnosis")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": \"DOC101\"}"))
                .andDo(print())
                .andExpect(status().isOk());

    }
}