package com.health.application.medhealth.dto;

import java.util.Date;

public class ExceptionFormat {
    private String message;
    private String details;
    private Date timestamp;

    public ExceptionFormat() {
    }

    public ExceptionFormat(String message, String details) {
        this.message = message;
        this.details = details;
        this.timestamp = new Date();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
