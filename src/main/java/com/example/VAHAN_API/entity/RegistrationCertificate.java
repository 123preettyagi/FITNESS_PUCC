package com.example.VAHAN_API.entity;

import java.util.Date;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "registration_certificates")
public class RegistrationCertificate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "rc_number", unique = true, nullable = false)
    @NotBlank(message = "RC Number is required")
    private String rcNumber;
    
    @OneToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    @NotNull(message = "Vehicle ID is required")
    private Vehicle vehicle;  
    
    
    // FOR DUPLICATE RC
  /*  @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false) // FOR DUPLICATE RC
    private Vehicle vehicle; */

 

    
    @Column(name = "owner_name", nullable = false)
    @NotBlank(message = "Owner name is required")
    @Size(max = 100, message = "Owner name must be less than 100 characters")
    private String ownerName;
    
    
    
    @Column(name = "father_husband_name")
    @Size(max = 100, message = "Father/Husband name must be less than 100 characters")
    private String fatherHusbandName;
    
    
    
    @Column(name = "permanent_address")
    @NotBlank(message = "Permanent address is required")
    @Size(max = 200, message = "Permanent address must be less than 200 characters")
    private String permanentAddress;
    
    @Column(name = "temporary_address")
    @Size(max = 200, message = "Temporary address must be less than 200 characters")
    private String temporaryAddress;
    
    
    @Column(name = "mobile_number")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Mobile number must be valid 10-digit Indian number")
    private String mobileNumber;
    
    @Column(name = "email")
    @Email(message = "Email must be valid")
    private String email;
    
    @Column(name = "issue_date")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Issue date is required")
    private Date issueDate;
    
    @Column(name = "expiry_date")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Expiry date is required")
    private Date expiryDate;
    
    @Column(name = "rc_status")
    private String rcStatus; // ACTIVE, EXPIRED, SUSPENDED, CANCELLED
    
    @Column(name = "rc_type")
    private String rcType; // ORIGINAL, DUPLICATE, SMART_CARD
    
    @Column(name = "is_digital_copy_available")
    private Boolean isDigitalCopyAvailable = false;
    
    @Column(name = "digital_rc_url")
    private String digitalRcUrl;
    
    @Column(name = "qr_code")
    private String qrCode;
    
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getRcNumber() { return rcNumber; }
    public void setRcNumber(String rcNumber) { this.rcNumber = rcNumber; }
    
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    
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
    
    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }
    
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
    
    public String getRcStatus() { return rcStatus; }
    public void setRcStatus(String rcStatus) { this.rcStatus = rcStatus; }
    
    public String getRcType() { return rcType; }
    public void setRcType(String rcType) { this.rcType = rcType; }
    
    public Boolean getIsDigitalCopyAvailable() { return isDigitalCopyAvailable; }
    public void setIsDigitalCopyAvailable(Boolean isDigitalCopyAvailable) { this.isDigitalCopyAvailable = isDigitalCopyAvailable; }
    
    public String getDigitalRcUrl() { return digitalRcUrl; }
    public void setDigitalRcUrl(String digitalRcUrl) { this.digitalRcUrl = digitalRcUrl; }
    
    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}