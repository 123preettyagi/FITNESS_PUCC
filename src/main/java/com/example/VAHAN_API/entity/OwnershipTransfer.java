package com.example.VAHAN_API.entity;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "ownership_transfers")
public class OwnershipTransfer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
    
    @Column(name = "transfer_type", nullable = false)
    private String transferType; // SALE, DEATH, AUCTION, INTER_STATE
    
    @Column(name = "previous_owner_name")
    private String previousOwnerName;
    
    @Column(name = "new_owner_name", nullable = false)
    private String newOwnerName;
    
    @Column(name = "new_owner_father_name")
    private String newOwnerFatherName;
    
    @Column(name = "new_owner_address")
    private String newOwnerAddress;
    
    @Column(name = "new_owner_mobile")
    private String newOwnerMobile;
    
    @Column(name = "new_owner_email")
    private String newOwnerEmail;
    
    @Column(name = "transfer_date")
    @Temporal(TemporalType.DATE)
    private Date transferDate;
    
    @Column(name = "sale_amount")
    private Double saleAmount;
    
    @Column(name = "sale_deed_number")
    private String saleDeedNumber;
    
    @Column(name = "death_certificate_number")
    private String deathCertificateNumber;
    
    @Column(name = "auction_reference")
    private String auctionReference;
    
    @Column(name = "noc_reference")
    private String nocReference;
    
    @Column(name = "is_inter_state")
    private Boolean isInterState = false;
    
    @Column(name = "from_state")
    private String fromState;
    
    @Column(name = "to_state")
    private String toState;
    
    @Column(name = "transfer_status")
    private String transferStatus; // PENDING, APPROVED, REJECTED, COMPLETED
    
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
    
    public String getTransferType() { return transferType; }
    public void setTransferType(String transferType) { this.transferType = transferType; }
    
    public String getPreviousOwnerName() { return previousOwnerName; }
    public void setPreviousOwnerName(String previousOwnerName) { this.previousOwnerName = previousOwnerName; }
    
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
    
    public String getNocReference() { return nocReference; }
    public void setNocReference(String nocReference) { this.nocReference = nocReference; }
    
    public Boolean getIsInterState() { return isInterState; }
    public void setIsInterState(Boolean isInterState) { this.isInterState = isInterState; }
    
    public String getFromState() { return fromState; }
    public void setFromState(String fromState) { this.fromState = fromState; }
    
    public String getToState() { return toState; }
    public void setToState(String toState) { this.toState = toState; }
    
    public String getTransferStatus() { return transferStatus; }
    public void setTransferStatus(String transferStatus) { this.transferStatus = transferStatus; }
    
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}