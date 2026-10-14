package com.example.VAHAN_API.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "insurance")
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationNumber;
    private String policyNumber;
    private String insurerName;

    private LocalDate startDate;
    private LocalDate endDate;

    private String status; // ACTIVE / EXPIRED / CLAIMED
    
    
    
    private Boolean injury;  // TRUE = Accident with injury, FALSE = No injury

    public Boolean getInjury() {
        return injury;
    }

    public void setInjury(Boolean injury) {
        this.injury = injury;
    }

    
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    
    
    
}