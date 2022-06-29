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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerDoctor(@RequestBody Doctor doctor) {
        Doctor dto = userService.saveDoctor(doctor);
        if (dto != null) {
            UserCreationMessageDTO messageDTO = new UserCreationMessageDTO(dto.getId(), "Doctor registered successfully");
            return new ResponseEntity<>(messageDTO, HttpStatus.CREATED);
        } else {
            throw new UnableToProcessException("Doctor is not created");
        }
    }

    @PostMapping("/list")
    public ResponseEntity<?> getUserDetails(@RequestBody FetchDetailsDTO fetchDetailsDTO) {
        String id = fetchDetailsDTO.getUserId();
        Doctor userDetails = (Doctor) userService.getUser(id, fetchDetailsDTO.getEmail());
        if (userDetails != null) {
            return new ResponseEntity<>(CustomUtils.generateUserData(userDetails), HttpStatus.OK);
        }
        throw new UnableToProcessException("Doctor detail not found");
    }
}
