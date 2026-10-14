package com.example.VAHAN_API.entity;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicles_vahan")
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "registration_number", unique = true, nullable = false)
    private String registrationNumber;
    
    @Column(name = "chassis_number", unique = true, nullable = false)
    private String chassisNumber;
    
    @Column(name = "engine_number", nullable = false)
    private String engineNumber;
    
    @Column(name = "vehicle_type", nullable = false)
    private String vehicleType;
    
    @Column(name = "vehicle_class")
    private String vehicleClass;
    
    @Column(name = "manufacturer")
    private String manufacturer;
    
    @Column(name = "model")
    private String model;
    
    @Column(name = "year_of_manufacture")
    private Integer yearOfManufacture;
    
    @Column(name = "fuel_type")
    private String fuelType;
    
    @Column(name = "color")
    private String color;
    
    @Column(name = "seating_capacity")
    private Integer seatingCapacity;
    
    @Column(name = "registration_date")
    @Temporal(TemporalType.DATE)
    private Date registrationDate;
    
    @Column(name = "registration_expiry_date")
    @Temporal(TemporalType.DATE)
    private Date registrationExpiryDate;
    
    @Column(name = "registration_status")
    private String registrationStatus; // PENDING, ACTIVE, EXPIRED, CANCELLED
    
    @Column(name = "registration_type")
    private String registrationType; // NEW, TEMPORARY, PERMANENT, BH_SERIES
    
    @Column(name = "is_fancy_number")
    private Boolean isFancyNumber = false;
    
    @Column(name = "fancy_number_category")
    private String fancyNumberCategory;
    
    @Column(name = "state_code")
    private String stateCode;
    
    @Column(name = "rto_office")
    private String rtoOffice;
    
    @Column(name = "owner_name", nullable = false)
    private String ownerName;
    
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
    
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    
    public String getChassisNumber() { return chassisNumber; }
    public void setChassisNumber(String chassisNumber) { this.chassisNumber = chassisNumber; }
    
    public String getEngineNumber() { return engineNumber; }
    public void setEngineNumber(String engineNumber) { this.engineNumber = engineNumber; }
    
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    
    public String getVehicleClass() { return vehicleClass; }
    public void setVehicleClass(String vehicleClass) { this.vehicleClass = vehicleClass; }
    
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public Integer getYearOfManufacture() { return yearOfManufacture; }
    public void setYearOfManufacture(Integer yearOfManufacture) { this.yearOfManufacture = yearOfManufacture; }
    
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public Integer getSeatingCapacity() { return seatingCapacity; }
    public void setSeatingCapacity(Integer seatingCapacity) { this.seatingCapacity = seatingCapacity; }
    
    public Date getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(Date registrationDate) { this.registrationDate = registrationDate; }
    
    public Date getRegistrationExpiryDate() { return registrationExpiryDate; }
    public void setRegistrationExpiryDate(Date registrationExpiryDate) { this.registrationExpiryDate = registrationExpiryDate; }
    
    public String getRegistrationStatus() { return registrationStatus; }
    public void setRegistrationStatus(String registrationStatus) { this.registrationStatus = registrationStatus; }
    
    public String getRegistrationType() { return registrationType; }
    public void setRegistrationType(String registrationType) { this.registrationType = registrationType; }
    
    public Boolean getIsFancyNumber() { return isFancyNumber; }
    public void setIsFancyNumber(Boolean isFancyNumber) { this.isFancyNumber = isFancyNumber; }
    
    public String getFancyNumberCategory() { return fancyNumberCategory; }
    public void setFancyNumberCategory(String fancyNumberCategory) { this.fancyNumberCategory = fancyNumberCategory; }
    
    public String getStateCode() { return stateCode; }
    public void setStateCode(String stateCode) { this.stateCode = stateCode; }
    
    public String getRtoOffice() { return rtoOffice; }
    public void setRtoOffice(String rtoOffice) { this.rtoOffice = rtoOffice; }
    
    
    
    public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}