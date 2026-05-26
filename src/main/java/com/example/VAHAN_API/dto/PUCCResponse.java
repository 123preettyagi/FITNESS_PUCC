package com.example.VAHAN_API.dto;

import java.time.LocalDate;

public class PUCCResponse 
{

    private String puccNumber;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private boolean valid;
    private Double coPercentage;
    private Integer hcPpm;

    
    
    public PUCCResponse(String puccNumber, LocalDate issueDate, LocalDate expiryDate,Double coPercentage, Integer hcPpm) {
        this.puccNumber = puccNumber;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        
        
        this.coPercentage = coPercentage;
        this.hcPpm = hcPpm;
        
        this.valid = !LocalDate.now().isAfter(expiryDate);
    }
    
    

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

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
    
    
}