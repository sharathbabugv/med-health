package com.health.application.medhealth.controller;

import com.health.application.medhealth.dto.*;
import com.health.application.medhealth.exceptions.UnableToProcessException;
import com.health.application.medhealth.services.MessagingService;
import com.health.application.medhealth.services.UserService;
import com.health.application.medhealth.utils.CustomUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final UserService userService;
    private final MessagingService<DiagnosisMail> mailMessagingService;

    @PostMapping("/list")
    public ResponseEntity<?> getUserDetails(@RequestBody FetchDetailsDTO fetchDetailsDTO) {
        String id = fetchDetailsDTO.getUserId();
        Doctor userDetails = (Doctor) userService.getUser(id, fetchDetailsDTO.getEmail());
        if (userDetails != null) {
            return new ResponseEntity<>(CustomUtils.generateUserData(userDetails), HttpStatus.OK);
        }
        throw new UnableToProcessException("Doctor detail not found");
    }

    @PostMapping("/send-diagnosis")
    public ResponseEntity<?> sendDiagnosis(@RequestBody Diagnosis diagnosis) {

        DiagnosisMail diagnosisMail = userService.sendPrescription(diagnosis);

        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("acknowledgement", "Email sent to " + diagnosisMail.getPatientEmail());
        messageMap.put("message", "Prescription has been sent. Thank you Dr." + diagnosisMail.getDoctorName());
        mailMessagingService.sendMessageToTopic("topic-send-prescription", diagnosisMail);
        return ResponseEntity.ok().body(messageMap);
    }

    @PostMapping("/diagnosis")
    public ResponseEntity<?> getDoctorDiagnosisList(@RequestBody GetDiagnosisForIdDTO getDiagnosisForIdDTO) {
        return ResponseEntity.ok(userService.findDiagnosisForId(getDiagnosisForIdDTO.getId(), "DOC"));
    }
}
