package com.example.VAHAN_API.dto;

import java.util.Date;

public class RcResponse {

    private Long id;
    private String ownerName;
    private String registrationNumber;
    private String chassisNumber;
    private String engineNumber;
    private String vehicleType;
    private String vehicleClass;
    private String manufacturer;
    private String model;
    private Integer yearOfManufacture;
    private String fuelType;
    private String color;
    private Integer seatingCapacity;
    private Date registrationDate;
    private Date registrationExpiryDate;
    private String registrationStatus;
    private String registrationType;
    private String stateCode;
    private String rtoOffice;

    // No-args constructor
    public RcResponse() {
    }

    // All-args constructor
    public RcResponse(Long id, String ownerName,String registrationNumber, String chassisNumber, String engineNumber,
                      String vehicleType, String vehicleClass, String manufacturer, String model,
                      Integer yearOfManufacture, String fuelType, String color, Integer seatingCapacity,
                      Date registrationDate, Date registrationExpiryDate, String registrationStatus,
                      String registrationType, String stateCode, String rtoOffice) {
        this.id = id;
        this.ownerName=ownerName;
        this.registrationNumber = registrationNumber;
        this.chassisNumber = chassisNumber;
        this.engineNumber = engineNumber;
        this.vehicleType = vehicleType;
        this.vehicleClass = vehicleClass;
        this.manufacturer = manufacturer;
        this.model = model;
        this.yearOfManufacture = yearOfManufacture;
        this.fuelType = fuelType;
        this.color = color;
        this.seatingCapacity = seatingCapacity;
        this.registrationDate = registrationDate;
        this.registrationExpiryDate = registrationExpiryDate;
        this.registrationStatus = registrationStatus;
        this.registrationType = registrationType;
        this.stateCode = stateCode;
        this.rtoOffice = rtoOffice;
    }

    // Getters and Setters

    
    
    public Long getId() {
        return id;
    }

    public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public void setId(Long id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(String vehicleClass) {
        this.vehicleClass = vehicleClass;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(Integer yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(Integer seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getRegistrationExpiryDate() {
        return registrationExpiryDate;
    }

    public void setRegistrationExpiryDate(Date registrationExpiryDate) {
        this.registrationExpiryDate = registrationExpiryDate;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getRtoOffice() {
        return rtoOffice;
    }

    public void setRtoOffice(String rtoOffice) {
        this.rtoOffice = rtoOffice;
    }
}
