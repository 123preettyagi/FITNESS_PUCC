package com.example.VAHAN_API.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OwnershipTransferRequest {
    
	
	@NotBlank(message = "Registration number is required")
    @Pattern(
    	regexp = "^[A-Z]{2}-[A-Z]{2}-\\d{4}-\\d{2}$",
        message = "Invalid registration number",
        groups= {TransferFull.class,TransferSale.class,TransferDeath.class,TransferAuction.class,TransferInterstate.class}
    )
    private String registrationNumber;
	
	@NotBlank(message = "Transfer type is required")
    @Pattern(
        regexp = "SALE|DEATH|AUCTION|INTER_STATE",
        message = "Transfer type must be SALE, DEATH, AUCTION, or INTER_STATE",
        groups=TransferFull.class
    )
    private String transferType; // SALE, DEATH, AUCTION, INTER_STATE
	
	
@NotBlank(message = "New owner name is required",groups= {TransferFull.class,TransferSale.class,TransferDeath.class,TransferAuction.class,TransferInterstate.class})
@Size(min = 3, max = 50, message = "Owner name must be 3–50 characters")
    private String newOwnerName;
	 
	@NotBlank(message = "Father name is required")
    private String newOwnerFatherName;
	
	@NotBlank(message = "Address is required")
    private String newOwnerAddress;
	
	@NotBlank(message = "Mobile number is required")
    @Pattern(
        regexp = "^[6-9]\\d{9}$",
        message = "Invalid Indian mobile number"
    )
    private String newOwnerMobile;
	
	@Email(message = "Invalid email format")
    private String newOwnerEmail;
	
	@NotNull(message = "Transfer date is required",groups= {TransferFull.class,TransferSale.class,TransferDeath.class,TransferAuction.class,TransferInterstate.class})
    @PastOrPresent(message = "Transfer date cannot be in future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date transferDate;
	
    private Double saleAmount;
    private String saleDeedNumber;
    private String deathCertificateNumber;
    private String auctionReference;
    private Boolean isInterState;
    
    @NotBlank(message = "From state is required",groups=TransferInterstate.class)
    @Size(min = 2, max = 50, message = "From state must be between 2 and 50 characters")
    private String fromState;
    
    @NotBlank(message = "To state is required",groups=TransferInterstate.class)
    @Size(min = 2, max = 50, message = "To state must be between 2 and 50 characters")
    private String toState;
    
    private String remarks;
    
    // Getters and Setters
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    
    public String getTransferType() { return transferType; }
    public void setTransferType(String transferType) { this.transferType = transferType; }
    
    public String getNewOwnerName() { return newOwnerName; }
    public void setNewOwnerName(String newOwnerName) { this.newOwnerName = newOwnerName; }
    
    public String getNewOwnerFatherName() { return newOwnerFatherName; }
    public void setNewOwnerFatherName(String newOwnerFatherName) { this.newOwnerFatherName = newOwnerFatherName; }
    
    public String getNewOwnerAddress() { return newOwnerAddress; }
    public void setNewOwnerAddress(String newOwnerAddress) { this.newOwnerAddress = newOwnerAddress; }
    
    public String getNewOwnerMobile() { return newOwnerMobile; }
    public void setNewOwnerMobile(String newOwnerMobile) { this.newOwnerMobile = newOwnerMobile; }
    
    public String getNewOwnerEmail() { return newOwnerEmail; }
    public void setNewOwnerEmail(String newOwnerEmail) { this.newOwnerEmail = newOwnerEmail; }
    
    public Date getTransferDate() { return transferDate; }
    public void setTransferDate(Date transferDate) { this.transferDate = transferDate; }
    
    public Double getSaleAmount() { return saleAmount; }
    public void setSaleAmount(Double saleAmount) { this.saleAmount = saleAmount; }
    
    public String getSaleDeedNumber() { return saleDeedNumber; }
    public void setSaleDeedNumber(String saleDeedNumber) { this.saleDeedNumber = saleDeedNumber; }
    
    public String getDeathCertificateNumber() { return deathCertificateNumber; }
    public void setDeathCertificateNumber(String deathCertificateNumber) { this.deathCertificateNumber = deathCertificateNumber; }
    
    public String getAuctionReference() { return auctionReference; }
    public void setAuctionReference(String auctionReference) { this.auctionReference = auctionReference; }
    
    public Boolean getIsInterState() { return isInterState; }
    public void setIsInterState(Boolean isInterState) { this.isInterState = isInterState; }
    
    public String getFromState() { return fromState; }
    public void setFromState(String fromState) { this.fromState = fromState; }
    
    public String getToState() { return toState; }
    public void setToState(String toState) { this.toState = toState; }
    
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    
    
    public interface TransferFull {}
    public interface TransferSale {}
    public interface TransferDeath {}
    public interface TransferAuction {}
    public interface TransferInterstate {}
    
}

