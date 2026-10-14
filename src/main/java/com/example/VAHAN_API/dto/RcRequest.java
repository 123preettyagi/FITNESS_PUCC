package com.example.VAHAN_API.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RcRequest {

    // ✅ Validation group marker interface
    public interface IssueRc {}

	
    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotBlank(message = "Last 5 digits of chassis number are required")
    @Pattern(
        regexp = "^[A-Za-z0-9]{5}$",
        message = "Chassis number must contain exactly last 5 alphanumeric characters"
    )
    private String chassisLast5;

    // Getters and Setters

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getChassisLast5() {
        return chassisLast5;
    }

    public void setChassisLast5(String chassisLast5) {
        this.chassisLast5 = chassisLast5;
    }
}

