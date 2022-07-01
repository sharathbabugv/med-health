package com.health.application.medhealth.controller;

import com.health.application.medhealth.dto.Doctor;
import com.health.application.medhealth.dto.Patient;
import com.health.application.medhealth.dto.UserCreationMessageDTO;
import com.health.application.medhealth.exceptions.UnableToProcessException;
import com.health.application.medhealth.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @PostMapping("/patient")
    public ResponseEntity<?> registerPatient(@RequestBody Patient patient) {
        Patient dto = userService.savePatient(patient);
        if (dto != null) {
            UserCreationMessageDTO messageDTO = new UserCreationMessageDTO(dto.getId(), "Patient registered successfully");
            return new ResponseEntity<>(messageDTO, HttpStatus.CREATED);
        } else {
            throw new UnableToProcessException("Patient is not created");
        }
    }

    @PostMapping("/doctor")
    public ResponseEntity<?> registerDoctor(@RequestBody Doctor doctor) {
        Doctor dto = userService.saveDoctor(doctor);
        if (dto != null) {
            UserCreationMessageDTO messageDTO = new UserCreationMessageDTO(dto.getId(), "Doctor registered successfully");
            return new ResponseEntity<>(messageDTO, HttpStatus.CREATED);
        } else {
            throw new UnableToProcessException("Doctor is not created");
        }
    }
}
