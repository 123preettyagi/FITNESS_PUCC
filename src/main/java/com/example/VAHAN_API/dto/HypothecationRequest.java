package com.example.VAHAN_API.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class HypothecationRequest {
    
	@NotBlank(message = "Registration number is mandatory",groups= {HypothecationAdd.class,HPTTerminate.class,HPTContinue.class,HPTManage.class})
    private String registrationNumber;

    @NotBlank(message = "Financier name is mandatory",groups=HypothecationAdd.class)
    @Size(min = 3, max = 100, message = "Financier name must be between 3 and 100 characters")
    private String financierName;

    @NotBlank(message = "Financier address is mandatory",groups= {HypothecationAdd.class,HPTManage.class})
    private String financierAddress;

    @NotBlank(message = "Financier contact is mandatory",groups= {HypothecationAdd.class,HPTManage.class})
    @Pattern(regexp = "^[0-9]{10}$", message = "Financier contact must be a valid 10-digit number")
    private String financierContact;

    @NotBlank(message = "Loan account number is mandatory",groups=HypothecationAdd.class)
    private String loanAccountNumber;

    @NotNull(message = "Loan amount is mandatory",groups=HypothecationAdd.class)
    @Positive(message = "Loan amount must be greater than 0")
    private Double loanAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)

    @NotNull(message = "Loan start date is mandatory",groups= {HypothecationAdd.class,HPTContinue.class})
    @PastOrPresent(message = "Loan start date cannot be in the future")
    private Date loanStartDate;

    
    
    @NotNull(message = "Loan end date is mandatory",groups=HypothecationAdd.class)
    @Future(message = "Loan end date must be in the future")
    private Date loanEndDate;

    @NotBlank(message = "Action is mandatory",groups= {HypothecationAdd.class,HPTManage.class})
    @Pattern(
        regexp = "ADD|TERMINATE|CONTINUE|UPDATE",
        message = "Action must be ADD, TERMINATE, CONTINUE or UPDATE"
    )
    private String action;

    @Size(max = 250, message = "Remarks cannot exceed 250 characters",groups= {HypothecationAdd.class,HPTTerminate.class,HPTContinue.class,HPTManage.class})
    private String remarks;

    
    // Getters and Setters
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    
    public String getFinancierName() { return financierName; }
    public void setFinancierName(String financierName) { this.financierName = financierName; }
    
    public String getFinancierAddress() { return financierAddress; }
    public void setFinancierAddress(String financierAddress) { this.financierAddress = financierAddress; }
    
    public String getFinancierContact() { return financierContact; }
    public void setFinancierContact(String financierContact) { this.financierContact = financierContact; }
    
    public String getLoanAccountNumber() { return loanAccountNumber; }
    public void setLoanAccountNumber(String loanAccountNumber) { this.loanAccountNumber = loanAccountNumber; }
    
    public Double getLoanAmount() { return loanAmount; }
    public void setLoanAmount(Double loanAmount) { this.loanAmount = loanAmount; }
    
    public Date getLoanStartDate() { return loanStartDate; }
    public void setLoanStartDate(Date loanStartDate) { this.loanStartDate = loanStartDate; }
    
    public Date getLoanEndDate() { return loanEndDate; }
    public void setLoanEndDate(Date loanEndDate) { this.loanEndDate = loanEndDate; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    
    
    public interface HypothecationAdd{};
    public interface HPTTerminate{};
    public interface HPTContinue{};
    public interface HPTManage{};
    
}