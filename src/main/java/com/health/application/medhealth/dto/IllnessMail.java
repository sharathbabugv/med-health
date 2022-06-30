package com.health.application.medhealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IllnessMail {
    private String appointmentId;
    private String doctorName;
    private String doctorEmail;
    private String patientName;
    private String patientContact;
    private String patientAge;
    private String patientEmail;
    private String patientIllness;
}