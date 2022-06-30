package com.health.application.medhealth.controller;

import com.health.application.medhealth.dto.*;
import com.health.application.medhealth.exceptions.UnableToProcessException;
import com.health.application.medhealth.services.MessagingService;
import com.health.application.medhealth.services.UserService;
import com.health.application.medhealth.utils.CustomUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final UserService userService;
    private final MessagingService<IllnessMail> mailMessagingService;

    @PostMapping("/list")
    public ResponseEntity<?> getUserDetails(@RequestBody FetchDetailsDTO fetchDetailsDTO) {
        String id = fetchDetailsDTO.getUserId();
        Patient userDetails = (Patient) userService.getUser(id, fetchDetailsDTO.getEmail());
        if (userDetails != null) {
            return new ResponseEntity<>(CustomUtils.generateUserData(userDetails), HttpStatus.OK);
        }
        throw new UnableToProcessException("Patient detail not found");
    }

    @PostMapping("/speciality")
    public ResponseEntity<?> getDoctorSpecialityList(@RequestBody SearchSpecialityDTO searchSpecialityDTO) {
        return ResponseEntity.ok().body(userService.findDoctorsBySpeciality(searchSpecialityDTO.getSpeciality()));
    }

    @PostMapping("/select-doctor")
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment bookAppointment) {
        return ResponseEntity.ok().body(userService.saveAppointment(bookAppointment));
    }

    @PostMapping("/send-mail")
    public ResponseEntity<?> sendEmail(@RequestBody Illness illness) {
        IllnessMail illnessMail = userService.sendMail(illness);
        this.mailMessagingService.sendMessageToTopic("topic-send-email", illnessMail);
        Map<String, String> map = new HashMap<>();
        map.put("acknowledgement", String.format("Email has been sent to Dr.%s at %s", illnessMail.getDoctorName(), illnessMail.getDoctorEmail()));
        map.put("message", String.format("Hi %s, your concern has been noted. Dr.%s will call you in 30 minutes", illnessMail.getPatientName(), illnessMail.getDoctorName()));
        return ResponseEntity.ok().body(map);
    }

    @PostMapping("/diagnosis")
    public ResponseEntity<?> getPatientDiagnosisList(@RequestBody GetDiagnosisForIdDTO getDiagnosisForIdDTO) {
        return ResponseEntity.ok(userService.findDiagnosisForId(getDiagnosisForIdDTO.getId(), "PAT"));
    }
}

// zookeeper-server-start.bat ../../config/zookeeper.properties
// kafka-server-start.bat ../../config/server.properties
