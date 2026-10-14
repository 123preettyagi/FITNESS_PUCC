package com.example.VAHAN_API.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class VehicleRegistrationRequest {
    
	@NotBlank(message = "Chassis number is required",groups= {NewVehicle.class,TemporaryVehicle.class,PermanentVehicle.class,BHSeries.class})
    @Size(min = 6, max = 20, message = "Chassis number must be 6–20 characters")
    private String chassisNumber;
	
	 @NotBlank(message = "Engine number is required",groups= {NewVehicle.class,TemporaryVehicle.class,PermanentVehicle.class,BHSeries.class})
	 @Size(min = 5, max = 20)
    private String engineNumber;
	 
	@NotBlank(message = "Vehicle type is required",groups= {NewVehicle.class,TemporaryVehicle.class,PermanentVehicle.class,BHSeries.class})
    private String vehicleType;
	
	@NotBlank(message = "Vehicle class is required",groups= {NewVehicle.class,TemporaryVehicle.class,PermanentVehicle.class,BHSeries.class})
    private String vehicleClass;
	
	@NotBlank(message = "Manufacturer is required",groups= {TemporaryVehicle.class,PermanentVehicle.class,BHSeries.class})
    private String manufacturer;
	
	 @NotBlank(message = "Model is required",groups= {TemporaryVehicle.class,PermanentVehicle.class,BHSeries.class})
    private String model;
	 
	 @NotNull(message = "Year of manufacture is required",groups= {NewVehicle.class,PermanentVehicle.class,BHSeries.class})
	 @Min(value = 1990, message = "Year must be >= 1990")
	 @Max(value = 2099, message = "Invalid year")
    private Integer yearOfManufacture;
	
	@NotBlank(message = "Fuel type is required",groups= {NewVehicle.class,TemporaryVehicle.class,PermanentVehicle.class,BHSeries.class})
    private String fuelType;
	
	@NotBlank(message = "Color is required",groups= {NewVehicle.class,TemporaryVehicle.class,PermanentVehicle.class,BHSeries.class})
    private String color;
	
	@NotNull(message = "Seating capacity is required",groups= {NewVehicle.class,TemporaryVehicle.class,PermanentVehicle.class,BHSeries.class})
    @Min(value = 1)
    @Max(value = 10)
    private Integer seatingCapacity;
	
	
	@NotBlank(message = "Registration type is required",groups= {NewVehicle.class})
    @Pattern(
        regexp = "TEMPORARY|PERMANENT|BH_SERIES",
        message = "Invalid registration type"
    ) 
    private String registrationType; // NEW, TEMPORARY, PERMANENT, BH_SERIES
	
	
    private Boolean isFancyNumber;
    private String fancyNumberCategory;
    
    @NotBlank(message = "State code is required",groups= {TemporaryVehicle.class,PermanentVehicle.class,BHSeries.class})
    @Pattern(regexp = "[A-Z]{2}", message = "State code must be 2 capital letters")
    private String stateCode;
    
    @NotBlank(message = "RTO office is required",groups= {TemporaryVehicle.class,PermanentVehicle.class,BHSeries.class})
    private String rtoOffice;
    
    @NotBlank(message = "Owner name is required",groups= {NewVehicle.class,TemporaryVehicle.class,PermanentVehicle.class,BHSeries.class})
    private String ownerName;
    
    @NotBlank(message = "Father/Husband name is required",groups= {NewVehicle.class})
    private String fatherHusbandName;
    
    @NotBlank(message = "Permanent address is required",groups= {NewVehicle.class,TemporaryVehicle.class,PermanentVehicle.class,BHSeries.class})
    private String permanentAddress;
    
    @NotBlank(message = "Mobile number is required",groups= {NewVehicle.class,TemporaryVehicle.class,PermanentVehicle.class,BHSeries.class})
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
    private String mobileNumber;
    
    @Email(message = "Invalid email format",groups= {NewVehicle.class,PermanentVehicle.class,BHSeries.class})
    private String email;
    
    // Getters and Setters
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
    
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    
    public String getFatherHusbandName() { return fatherHusbandName; }
    public void setFatherHusbandName(String fatherHusbandName) { this.fatherHusbandName = fatherHusbandName; }
    
    public String getPermanentAddress() { return permanentAddress; }
    public void setPermanentAddress(String permanentAddress) { this.permanentAddress = permanentAddress; }
    
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    
    public interface NewVehicle {};
    public interface TemporaryVehicle{};
    public interface PermanentVehicle{};
    public interface BHSeries{};
    
    
    
    
}