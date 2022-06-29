package com.health.application.medhealth.dto;

public class UserCreationMessageDTO {

    private String id;
    private String message;

    public UserCreationMessageDTO() {
    }

    public UserCreationMessageDTO(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
