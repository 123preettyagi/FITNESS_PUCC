package com.example.VAHAN_API.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TaxRequest {

	
	@NotBlank(message = "Registration number is required")
    private String registrationNumber;
	
	@NotBlank(message = "Tax type is required")
    private String taxType;
    
	  @NotBlank(message = "Tax period is required")
    private String taxPeriod;
    
	  //  @NotNull(message = "Amount is required")
	  //@Positive(message = "Amount must be greater than zero")
    private Double amount;

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getTaxPeriod() {
        return taxPeriod;
    }

    public void setTaxPeriod(String taxPeriod) {
        this.taxPeriod = taxPeriod;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
