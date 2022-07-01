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

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @MockBean
    private UserService service;

    @MockBean
    private MessagingService<IllnessMail> illnessMailMessagingService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "developer", authorities = {"ROLE_PATIENT"})
    public void getUserDetails() throws Exception {
        Patient patient = new Patient("PAT103", "test@gmail.com", "password", "test", "M",
                LocalDate.of(1996, 6, 6), "1234567890");

        when(service.getUser(any(), any())).thenReturn(patient);

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/patient/list")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": \"PAT103\",\"email\": \"test@gmail.com\"}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "developer", authorities = {"ROLE_PATIENT"})
    public void getDoctorSpecialityList() throws Exception{
        DoctorSpecialityDTO doctorSpecialityDTO = new DoctorSpecialityDTO();
        doctorSpecialityDTO.setExperience("10 years");
        doctorSpecialityDTO.setId("1");
        doctorSpecialityDTO.setName("Dr.Jack");
        doctorSpecialityDTO.setQualification("MBBS");

        when(service.findDoctorsBySpeciality("ENT")).thenReturn(List.of(doctorSpecialityDTO));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/patient/speciality")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientId\": \"PAT103\",\"speciality\": \"ENT\"}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "developer", authorities = {"ROLE_PATIENT"})
    public void bookAppointment() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/patient/select-doctor")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1,\"patientId\": \"PAT103\",\"doctorId\": \"DOC101\"}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "developer", authorities = {"ROLE_PATIENT"})
    public void sendEmail() throws Exception{

        IllnessMail illnessMail = new IllnessMail();
        illnessMail.setPatientName("Test");
        illnessMail.setPatientAge("26");
        illnessMail.setDoctorName("Dr.Test");
        illnessMail.setPatientIllness("High Fever");

        when(service.sendMail(any(Illness.class))).thenReturn(illnessMail);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/patient/send-mail")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"appointmentId\": \"1\",\"patientId\": \"PAT103\",\"doctorId\": \"DOC101\",\"illness\": \"High fever\"}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "developer", authorities = {"ROLE_PATIENT"})
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

        when(service.findDiagnosisForId("PAT101", "PAT")).thenReturn(List.of(diagnosis));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/patient/diagnosis")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": \"PAT101\"}"))
                .andDo(print())
                .andExpect(status().isOk());

    }
}