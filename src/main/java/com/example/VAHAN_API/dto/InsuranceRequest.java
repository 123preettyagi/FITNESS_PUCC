package com.example.VAHAN_API.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class InsuranceRequest
{

	
	@NotBlank(message = "Registration number is required")
  /*  @Pattern(
        regexp = "^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$",
        message = "Invalid registration number format (e.g. DL01AB1234)"
    ) */
    private String registrationNumber;
	
	@NotBlank(message = "Policy number is required")
    private String policyNumber;
	
	@NotBlank(message = "Insurer name is required")
    private String insurerName;
	
	@NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;
	
	@NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;
    
    
    private Boolean injury; // optional during linking
    
	public Boolean getInjury() {
		return injury;
	}
	public void setInjury(Boolean injury) {
		this.injury = injury;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getInsurerName() {
		return insurerName;
	}
	public void setInsurerName(String insurerName) {
		this.insurerName = insurerName;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

 
    
    
    
}