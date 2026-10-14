package com.example.VAHAN_API.service;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.VAHAN_API.dto.RcRequest;
import com.example.VAHAN_API.entity.RegistrationCertificate;
import com.example.VAHAN_API.entity.Vehicle;
import com.example.VAHAN_API.repository.RegistrationCertificateRepository;
import com.example.VAHAN_API.repository.VehicleRepository;

@Service
public class RcService {
    
    @Autowired
    private RegistrationCertificateRepository rcRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
  
    public RegistrationCertificate issueRc(RcRequest request) {

        // 1. Fetch the vehicle first
        Vehicle vehicle = vehicleRepository
                .findByRegistrationNumber(request.getRegistrationNumber())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        // 2. Check if an RC already exists for this vehicle
        rcRepository.findByRegistrationNumber(request.getRegistrationNumber())
                .ifPresent(rc -> {
                    throw new RuntimeException(
                        "RC already issued for this vehicle: " + rc.getRegistrationNumber());
                });

        // 3. Validate chassis last-5 match (security check)
        String chassis = vehicle.getChassisNumber();
        if (chassis == null || chassis.length() < 5) {
            throw new RuntimeException("Vehicle chassis number is invalid");
        }
        String last5 = chassis.substring(chassis.length() - 5);
        if (!last5.equalsIgnoreCase(request.getChassisLast5())) {
            throw new RuntimeException("Chassis number mismatch");
        }

        // 4. Build the new RC from the Vehicle's data
        RegistrationCertificate rc = new RegistrationCertificate();
        rc.setOwnerName(vehicle.getOwnerName());
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
        rc.setStateCode(vehicle.getStateCode());
        rc.setRtoOffice(vehicle.getRtoOffice());
        

        // 5. Registration lifecycle fields
        Date now = new Date();
        rc.setRegistrationDate(now);
      // rc.setRegistrationExpiryDate(calculateRcExpiryDate(now));
        rc.setRegistrationExpiryDate(calculateRcExpiryDate(now, vehicle.getFuelType()));
        rc.setRegistrationStatus("ACTIVE");
        rc.setRegistrationType("NEW");   // or request.getRcType() if RcRequest had it

        return rcRepository.save(rc);
    }
    
  
    
    private Date calculateRcExpiryDate(Date registrationDate, String fuelType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(registrationDate);

        int validityYears;

        switch (fuelType.toUpperCase()) {
            case "PETROL":
            case "DIESEL":
            case "CNG":
                validityYears = 15;   // 15 years for petrol/diesel/CNG
                break;

            case "ELECTRIC":
            case "EV":
                validityYears = 15;   // 15 years for electric (adjust as per RTO rules)
                break;

            case "LPG":
                validityYears = 15;
                break;

            default:
                validityYears = 15;   // fallback default
        }

        calendar.add(Calendar.YEAR, validityYears);
        return calendar.getTime();
    }
 
    

/**
 * Runs every day at midnight.
 * Updates expired ACTIVE RCs to INACTIVE.
 */
private static final Logger log =
        LoggerFactory.getLogger(RcService.class);




//@Scheduled(cron = "0 * * * * *")
@Scheduled(cron = "0 0 0 * * ?") 
@Transactional
public void deactivateExpiredRcs() {

log.info("RC expiry scheduler started at {}", new Date());

int updatedCount = rcRepository.updateExpiredRcStatus();

log.info("Expired RCs updated: {}", updatedCount);

log.info(
"RC expiry scheduler completed. Updated RCs: {}",
updatedCount
);
}

public boolean rcExists(String registrationNumber) 
{
    return rcRepository.findByRegistrationNumber(registrationNumber).isPresent();
}

   

    
    
    
}