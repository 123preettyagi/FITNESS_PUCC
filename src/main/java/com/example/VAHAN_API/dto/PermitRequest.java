package com.example.VAHAN_API.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PermitRequest {

    @NotBlank
    private String registrationNumber;

    @NotBlank
    private String permitType;

    @NotNull
    private LocalDate issueDate;

    @NotNull
    private LocalDate expiryDate;

    @NotBlank
    private String issuingAuthority;

    // Default Constructor
    public PermitRequest() {
    }

    // Parameterized Constructor
    public PermitRequest(String registrationNumber, String permitType,
                         LocalDate issueDate, LocalDate expiryDate,
                         String issuingAuthority) {
        this.registrationNumber = registrationNumber;
        this.permitType = permitType;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.issuingAuthority = issuingAuthority;
    }

    // Getters and Setters

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getPermitType() {
        return permitType;
    }

    public void setPermitType(String permitType) {
        this.permitType = permitType;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    // toString() Method
    @Override
    public String toString() {
        return "PermitRequest{" +
                "registrationNumber='" + registrationNumber + '\'' +
                ", permitType='" + permitType + '\'' +
                ", issueDate=" + issueDate +
                ", expiryDate=" + expiryDate +
                ", issuingAuthority='" + issuingAuthority + '\'' +
                '}';
    }
    
    
}