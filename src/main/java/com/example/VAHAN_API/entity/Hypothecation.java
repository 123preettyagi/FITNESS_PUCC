package com.example.VAHAN_API.entity;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "hypothecations")
public class Hypothecation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
    
    @Column(name = "financier_name", nullable = false)
    private String financierName;
    
    @Column(name = "financier_address")
    private String financierAddress;
    
    @Column(name = "financier_contact")
    private String financierContact;
    
    @Column(name = "loan_account_number")
    private String loanAccountNumber;
    
    @Column(name = "loan_amount")
    private Double loanAmount;
    
    @Column(name = "loan_start_date")
    @Temporal(TemporalType.DATE)
    private Date loanStartDate;
    
    @Column(name = "loan_end_date")
    @Temporal(TemporalType.DATE)
    private Date loanEndDate;
    
    @Column(name = "hypothecation_status")
    private String hypothecationStatus; // ACTIVE, CLOSED, TERMINATED
    
    @Column(name = "noc_issued")
    private Boolean nocIssued = false;
    
    @Column(name = "noc_issue_date")
    @Temporal(TemporalType.DATE)
    private Date nocIssueDate;
    
    @Column(name = "noc_document_url")
    private String nocDocumentUrl;
    
    @Column(name = "digital_noc_token")
    private String digitalNocToken;
    
    @Column(name = "remarks")
    private String remarks;
    
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
    
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    
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
    
    public String getHypothecationStatus() { return hypothecationStatus; }
    public void setHypothecationStatus(String hypothecationStatus) { this.hypothecationStatus = hypothecationStatus; }
    
    public Boolean getNocIssued() { return nocIssued; }
    public void setNocIssued(Boolean nocIssued) { this.nocIssued = nocIssued; }
    
    public Date getNocIssueDate() { return nocIssueDate; }
    public void setNocIssueDate(Date nocIssueDate) { this.nocIssueDate = nocIssueDate; }
    
    public String getNocDocumentUrl() { return nocDocumentUrl; }
    public void setNocDocumentUrl(String nocDocumentUrl) { this.nocDocumentUrl = nocDocumentUrl; }
    
    public String getDigitalNocToken() { return digitalNocToken; }
    public void setDigitalNocToken(String digitalNocToken) { this.digitalNocToken = digitalNocToken; }
    
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}