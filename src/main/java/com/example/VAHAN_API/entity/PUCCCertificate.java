package com.example.VAHAN_API.entity;

import java.time.LocalDate;
import jakarta.persistence.Id;

import jakarta.persistence.*;

@Entity
@Table(name = "pucc_certificate")
public class PUCCCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String registrationNumber;

    @Column(nullable = false)
    private String puccNumber;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private String testingCenter;

    @Column(nullable = false)
    private Double coPercentage;   // CO %

    @Column(nullable = false)
    private Integer hcPpm;         // HC ppm
    
    
    
    
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

	public String getPuccNumber() {
		return puccNumber;
	}

	public void setPuccNumber(String puccNumber) {
		this.puccNumber = puccNumber;
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

	public String getTestingCenter() {
		return testingCenter;
	}

	public void setTestingCenter(String testingCenter) {
		this.testingCenter = testingCenter;
	}

    
    
    
}