package com.example.VAHAN_API.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class PUCCRequest {

    @NotBlank
    private String registrationNumber;

    @NotNull
    private LocalDate issueDate;

    @Min(value = 6, message = "PUCC validity must be exactly 6 months")
    @Max(value = 6, message = "PUCC validity must be exactly 6 months")
    @NotNull
    private Integer validityMonths;

    @NotBlank
    private String testingCenter;

    
    @DecimalMin(value = "0.5", message = "CO must be at least 0.5%")
    @DecimalMax(value = "3.5", message = "CO must be at most 3.5%")
    private Double coPercentage;

    @Min(value = 750, message = "HC must be minimum 750 ppm")
    @Max(value = 1500, message = "HC must be maximum 1500 ppm")
    private Integer hcPpm;
    
    
    
    
	public Double getCoPercentage() {
		return coPercentage;
	}

	public void setCoPercentage(Double coPercentage) {
		this.coPercentage = coPercentage;
	}

	public Integer getHcPpm() {
		return hcPpm;
	}

	public void setHcPpm(Integer hcPpm) {
		this.hcPpm = hcPpm;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public LocalDate getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	public Integer getValidityMonths() {
		return validityMonths;
	}

	public void setValidityMonths(Integer validityMonths) {
		this.validityMonths = validityMonths;
	}

	public String getTestingCenter() {
		return testingCenter;
	}

	public void setTestingCenter(String testingCenter) {
		this.testingCenter = testingCenter;
	}

    
    
    
    
}
