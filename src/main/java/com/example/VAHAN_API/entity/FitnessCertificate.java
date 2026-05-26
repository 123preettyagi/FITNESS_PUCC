package com.example.VAHAN_API.entity;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "fitness_certificates")
public class FitnessCertificate {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@Column(nullable = false, unique = true)
private String registrationNumber;


@Temporal(TemporalType.DATE)
private Date issuedDate;


@Temporal(TemporalType.DATE)
private Date expiryDate;


private String issuingAuthority;


private String status; // VALID / EXPIRED


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


public Date getIssuedDate() {
	return issuedDate;
}


public void setIssuedDate(Date issuedDate) {
	this.issuedDate = issuedDate;
}


public Date getExpiryDate() {
	return expiryDate;
}


public void setExpiryDate(Date expiryDate) {
	this.expiryDate = expiryDate;
}


public String getIssuingAuthority() {
	return issuingAuthority;
}


public void setIssuingAuthority(String issuingAuthority) {
	this.issuingAuthority = issuingAuthority;
}


public String getStatus() {
	return status;
}


public void setStatus(String status) {
	this.status = status;
}



}