package com.health.application.medhealth.dto;

public class DoctorSpecificDTO extends UserDTO {

    private String education = null;
    private String speciality = null;
    private String experience = null;

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
