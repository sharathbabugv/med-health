package com.health.application.medhealth.utils;

import com.health.application.medhealth.dto.Doctor;
import com.health.application.medhealth.dto.DoctorSpecificDTO;
import com.health.application.medhealth.dto.Patient;
import com.health.application.medhealth.dto.UserDTO;
import com.health.application.medhealth.exceptions.UnableToProcessException;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomUtils {
    public static boolean isPatientValid(Patient patient) {
        if (isStringNotNull(patient.getName(), 2)) {
            if (isStringNotNull(patient.getPassword(), 6)) {
                if (isStringNotNull(patient.getPhoneNumber()) && patient.getDateOfBirth() != null &&
                        isStringNotNull(patient.getEmail())) {
                    return true;
                } else {
                    throw new UnableToProcessException("Please enter all the required fields to register");
                }
            } else {
                throw new UnableToProcessException("Password is either empty or doesn't match the minimum characters of 6");
            }
        }
        throw new UnableToProcessException("Username is either empty or doesn't match the minimum characters of 3");
    }

    public static boolean isDoctorValid(Doctor doctor) {
        if (isStringNotNull(doctor.getName(), 2)) {
            if (isStringNotNull(doctor.getPassword(), 6)) {
                if (isStringNotNull(doctor.getPhoneNumber())
                        && isStringNotNull(doctor.getQualification()) && doctor.getWorkStartDate() != null
                        && isStringNotNull(doctor.getSpeciality())
                        && isStringNotNull(doctor.getEmail())
                        && doctor.getDateOfBirth() != null) {
                    return true;
                } else {
                    throw new UnableToProcessException("Please enter all the required fields to register");
                }
            }
            throw new UnableToProcessException("Password is either empty or doesn't match the minimum characters of 6");
        }
        throw new UnableToProcessException("Username is either empty or doesn't match the minimum characters of 3");
    }

    public static boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-z0-9](\\.?[a-z0-9]){5,}@gmail\\.com$");
        Matcher mat = pattern.matcher(email);
        return mat.matches();
    }

    public static boolean isStringNotNull(String value) {
        return isStringNotNull(value, 0);
    }

    private static boolean isStringNotNull(String value, int length) {
        if (length == 0) {
            return value != null && !value.trim().isEmpty();
        } else {
            return value != null && !value.trim().isEmpty() && value.trim().length() > length;
        }

    }

    public static UserDTO generateUserData(Patient patient) {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(patient.getAuthorities().toString().replace("[", "").replace("]", ""));
        userDTO.setDateOfBirth(patient.getDateOfBirth());
        userDTO.setEmail(patient.getEmail());
        userDTO.setId(patient.getId());
        userDTO.setName(patient.getName());
        userDTO.setPhoneNumber(patient.getPhoneNumber());
        userDTO.setGender(patient.getGender());
        return userDTO;
    }

    public static DoctorSpecificDTO generateUserData(Doctor doctor) {
        DoctorSpecificDTO doctorSpecificDTO = new DoctorSpecificDTO();
        doctorSpecificDTO.setRole(doctor.getAuthorities().toString().replace("[", "").replace("]", ""));
        doctorSpecificDTO.setDateOfBirth(doctor.getDateOfBirth());
        doctorSpecificDTO.setEmail(doctor.getEmail());
        doctorSpecificDTO.setId(doctor.getId());
        doctorSpecificDTO.setName(doctor.getName());
        doctorSpecificDTO.setExperience(doctor.getExperience());
        doctorSpecificDTO.setPhoneNumber(doctor.getPhoneNumber());
        doctorSpecificDTO.setGender(doctor.getGender());
        doctorSpecificDTO.setEducation(doctor.getQualification());
        doctorSpecificDTO.setSpeciality(doctor.getSpeciality());
        return doctorSpecificDTO;
    }

    public static String getDifferenceBetweenDates(LocalDate start) {
        Period period = Period.between(start, LocalDate.now());
        int year = period.getYears();
        int month = period.getMonths();
        int days = period.getDays();

        if (year == 0) {
            if (month == 0) {
                return days + " days";
            } else {
                return month + (month == 1 ? " month" : " months");
            }
        } else {
            return year + (year == 1 ? " year" : " years");
        }
    }

}
