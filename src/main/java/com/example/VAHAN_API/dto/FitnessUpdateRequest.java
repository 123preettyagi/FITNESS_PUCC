package com.example.VAHAN_API.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FitnessUpdateRequest {

	 @NotNull(message = "issuedDate is required")
	 @JsonFormat(pattern = "yyyy-MM-dd")
    private Date issuedDate;

	 @NotNull(message = "expiryDate is required")
	 @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;

	 
	 @NotBlank(message = "issuingAuthority is required")
    private String issuingAuthority;

	 @NotBlank(message = "status is required")
    private String status; // VALID / EXPIRED

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