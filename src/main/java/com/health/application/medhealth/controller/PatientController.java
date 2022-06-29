package com.health.application.medhealth.controller;

import com.health.application.medhealth.dto.Doctor;
import com.health.application.medhealth.dto.FetchDetailsDTO;
import com.health.application.medhealth.dto.Patient;
import com.health.application.medhealth.dto.UserCreationMessageDTO;
import com.health.application.medhealth.exceptions.UnableToProcessException;
import com.health.application.medhealth.services.UserService;
import com.health.application.medhealth.utils.CustomUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerPatient(@RequestBody Patient patient) {
        Patient dto = userService.savePatient(patient);
        if (dto != null) {
            UserCreationMessageDTO messageDTO = new UserCreationMessageDTO(dto.getId(), "Patient registered successfully");
            return new ResponseEntity<>(messageDTO, HttpStatus.CREATED);
        } else {
            throw new UnableToProcessException("Patient is not created");
        }
    }

    @PostMapping("/list")
    public ResponseEntity<?> getUserDetails(@RequestBody FetchDetailsDTO fetchDetailsDTO) {
        String id = fetchDetailsDTO.getUserId();
        Patient userDetails = (Patient) userService.getUser(id, fetchDetailsDTO.getEmail());
        if (userDetails != null) {
            return new ResponseEntity<>(CustomUtils.generateUserData(userDetails), HttpStatus.OK);
        }
        throw new UnableToProcessException("Patient detail not found");
    }
}
