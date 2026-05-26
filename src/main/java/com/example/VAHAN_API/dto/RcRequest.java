package com.example.VAHAN_API.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RcRequest {
    
	
	@NotBlank(message = "Registration number is required",groups=IssueRc.class)
    private String registrationNumber;
	
	//@NotBlank(message = "RC Number is required")
    private String rcNumber;
    
    @NotBlank(message = "Owner name is required",groups=IssueRc.class)
    @Size(max = 100)
    private String ownerName;
    
    @Size(max = 100,groups=IssueRc.class)
    private String fatherHusbandName;
    
    @NotBlank(message = "Permanent address is required",groups=IssueRc.class)
    @Size(max = 200)
    private String permanentAddress;
    
    @Size(max = 200,groups=IssueRc.class)
    private String temporaryAddress;
  
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number",groups=IssueRc.class)
    private String mobileNumber;
    
    @Email(message = "Invalid email",groups=IssueRc.class)
    private String email;
    
    @NotBlank(message = "RC type is required",groups=IssueRc.class)
    private String rcType; // ORIGINAL, DUPLICATE, SMART_CARD
    
    // Getters and Setters
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    
    public String getRcNumber() { return rcNumber; }
    public void setRcNumber(String rcNumber) { this.rcNumber = rcNumber; }
    
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    
    public String getFatherHusbandName() { return fatherHusbandName; }
    public void setFatherHusbandName(String fatherHusbandName) { this.fatherHusbandName = fatherHusbandName; }
    
    public String getPermanentAddress() { return permanentAddress; }
    public void setPermanentAddress(String permanentAddress) { this.permanentAddress = permanentAddress; }
    
    public String getTemporaryAddress() { return temporaryAddress; }
    public void setTemporaryAddress(String temporaryAddress) { this.temporaryAddress = temporaryAddress; }
    
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRcType() { return rcType; }
    public void setRcType(String rcType) { this.rcType = rcType; }
    
    
    
    
    public interface IssueRc{};
    
}