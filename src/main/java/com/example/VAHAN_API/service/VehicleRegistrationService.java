package com.example.VAHAN_API.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.VAHAN_API.dto.VehicleRegistrationRequest;
import com.example.VAHAN_API.entity.RegistrationCertificate;
import com.example.VAHAN_API.entity.Vehicle;
import com.example.VAHAN_API.repository.RegistrationCertificateRepository;
import com.example.VAHAN_API.repository.VehicleRepository;

@Service
public class VehicleRegistrationService 
{
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private RegistrationCertificateRepository rcRepository;
    
    public Vehicle registerNewVehicle(VehicleRegistrationRequest request) 
    {
    	
    	
        // Check if vehicle already exists
        if (vehicleRepository.findByChassisNumber(request.getChassisNumber()).isPresent()) 
        {
            throw new RuntimeException("Vehicle with this chassis number already exists");
        }
        
        if (vehicleRepository.findByEngineNumber(request.getEngineNumber()).isPresent()) 
        {
            throw new RuntimeException("Vehicle with this engine number already exists");
        }
        
        // Generate registration number
        String registrationNumber = generateRegistrationNumber(request);
        
        // Create vehicle entity
        Vehicle vehicle = new Vehicle();
        vehicle.setChassisNumber(request.getChassisNumber());
        vehicle.setEngineNumber(request.getEngineNumber());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setVehicleClass(request.getVehicleClass());
        vehicle.setManufacturer(request.getManufacturer());
        vehicle.setModel(request.getModel());
        vehicle.setYearOfManufacture(request.getYearOfManufacture());
        vehicle.setFuelType(request.getFuelType());
        vehicle.setColor(request.getColor());
        vehicle.setSeatingCapacity(request.getSeatingCapacity());
        vehicle.setRegistrationNumber(registrationNumber);
        vehicle.setRegistrationType(request.getRegistrationType());
        vehicle.setIsFancyNumber(request.getIsFancyNumber());
        vehicle.setFancyNumberCategory(request.getFancyNumberCategory());
        vehicle.setStateCode(request.getStateCode());
        vehicle.setRtoOffice(request.getRtoOffice());
        vehicle.setOwnerName(request.getOwnerName());
        vehicle.setRegistrationDate(new Date());
        vehicle.setRegistrationStatus("PENDING");
        
        // Calculate expiry date based on registration type
        Date expiryDate = calculateExpiryDate(request.getRegistrationType());
        vehicle.setRegistrationExpiryDate(expiryDate);
        
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        
        // Create RC
      //  createRegistrationCertificate(savedVehicle, request);
        
        return savedVehicle;
        
        
    }
    
    
   
    /**
     * Check if a vehicle registration number exists.
     */
    public boolean vehicleExists(String registrationNumber) 
    {
        return vehicleRepository.findByRegistrationNumber(registrationNumber).isPresent();
    }
    
    private String generateRegistrationNumber(VehicleRegistrationRequest request) 
    {
        // Format: StateCode-RTOOffice-XXXX-YYYY
        String stateCode = request.getStateCode();
        String rtoCode = request.getRtoOffice().substring(0, 2).toUpperCase();
        String randomNum = String.format("%04d", (int)(Math.random() * 10000));
        String yearCode = String.valueOf(request.getYearOfManufacture()).substring(2);
        
        return stateCode + "-" + rtoCode + "-" + randomNum + "-" + yearCode;
        
    }
    
    
    
    private Date calculateExpiryDate(String registrationType) 
    {
        Date date = new Date();
        switch (registrationType) 
        {
        
        
            case "TEMPORARY":
                // 1 month for temporary registration
                return new Date(date.getTime() + (30L * 24 * 60 * 60 * 1000));
            case "PERMANENT":
                // 15 years for permanent registration
                return new Date(date.getTime() + (15L * 365 * 24 * 60 * 60 * 1000));
            case "BH_SERIES":
                // 2 years for BH series
                return new Date(date.getTime() + (2L * 365 * 24 * 60 * 60 * 1000));
            default:
                return new Date(date.getTime() + (15L * 365 * 24 * 60 * 60 * 1000));
                
        }
    }
    /*
    private void createRegistrationCertificate(Vehicle vehicle, VehicleRegistrationRequest request) 
    {
    	RegistrationCertificate rc = new RegistrationCertificate();

        // Vehicle details
        rc.setRegistrationNumber(vehicle.getRegistrationNumber());
        rc.setChassisNumber(vehicle.getChassisNumber());
        rc.setEngineNumber(vehicle.getEngineNumber());
        rc.setVehicleType(vehicle.getVehicleType());
        rc.setVehicleClass(vehicle.getVehicleClass());
        rc.setManufacturer(vehicle.getManufacturer());
        rc.setModel(vehicle.getModel());
        rc.setYearOfManufacture(vehicle.getYearOfManufacture());
        rc.setFuelType(vehicle.getFuelType());
        rc.setColor(vehicle.getColor());
        rc.setSeatingCapacity(vehicle.getSeatingCapacity());

        // Registration details
        rc.setRegistrationDate(vehicle.getRegistrationDate());
        rc.setRegistrationExpiryDate(vehicle.getRegistrationExpiryDate());
        rc.setRegistrationStatus("ACTIVE");
        rc.setRegistrationType("PERMANENT");

        // RTO details
        rc.setStateCode(vehicle.getStateCode());
        rc.setRtoOffice(vehicle.getRtoOffice());

        // Save RC
        rcRepository.save(rc);
    } */
    
    public Vehicle getVehicleByRegistrationNumber(String registrationNumber)
    {
        return vehicleRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }
    
  /*
    public Vehicle updateRegistrationNumber(String oldRegistrationNumber, String newRegistrationNumber) 
    {
        Vehicle vehicle = getVehicleByRegistrationNumber(oldRegistrationNumber);
        vehicle.setRegistrationNumber(newRegistrationNumber);
        vehicle.setUpdatedAt(new Date());
        return vehicleRepository.save(vehicle);
    }
    
    public Vehicle assignFancyNumber(String registrationNumber, String fancyNumber) {
        Vehicle vehicle = getVehicleByRegistrationNumber(registrationNumber);
        vehicle.setRegistrationNumber(fancyNumber);
        vehicle.setIsFancyNumber(true);
        vehicle.setFancyNumberCategory("VIP");
        vehicle.setUpdatedAt(new Date());
        return vehicleRepository.save(vehicle);
    }
    
    */
}