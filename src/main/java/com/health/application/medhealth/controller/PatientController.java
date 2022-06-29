package com.health.application.medhealth.controller;

import com.health.application.medhealth.dto.*;
import com.health.application.medhealth.exceptions.UnableToProcessException;
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
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final UserService userService;
    private final KafkaTemplate<String, Illness> kafkaTemplate;

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
    public ResponseEntity<?> bookAppointment(@RequestBody Illness illness) {
        kafkaTemplate.send("topic-send-email", illness);

        Map<String, String> map = new HashMap<>();
        map.put("acknowledgement", "Email has been sent to Dr.Jack at jack@abcd.com");
        map.put("message", "Hi User, your concern has been noted. Dr.Jack will call you in 30 minutes");
        return ResponseEntity.ok().body(map);
    }
}
