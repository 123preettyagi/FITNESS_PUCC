package com.example.VAHAN_API.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.VAHAN_API.dto.HypothecationRequest;
import com.example.VAHAN_API.entity.Hypothecation;
import com.example.VAHAN_API.entity.RegistrationCertificate;
import com.example.VAHAN_API.entity.Vehicle;
import com.example.VAHAN_API.repository.HypothecationRepository;
import com.example.VAHAN_API.repository.RegistrationCertificateRepository;
import com.example.VAHAN_API.repository.VehicleRepository;

@Service
public class HypothecationService {
    
    @Autowired
    private HypothecationRepository hypothecationRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    public Hypothecation manageHypothecation(HypothecationRequest request) {
        Vehicle vehicle = vehicleRepository.findByRegistrationNumber(request.getRegistrationNumber())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        
        Hypothecation hypothecation = new Hypothecation();
        
        switch (request.getAction()) {
            case "ADD":
                // Check if vehicle already has active hypothecation
                hypothecationRepository.findByVehicleRegistrationNumberAndHypothecationStatus(
                    request.getRegistrationNumber(), "ACTIVE")
                    .ifPresent(h -> {
                        throw new RuntimeException("Vehicle already has active hypothecation");
                    });
                
                hypothecation = createNewHypothecation(vehicle, request);
                break;
                
            case "TERMINATE":
                hypothecation = terminateHypothecation(vehicle, request);
                break;
                
            case "CONTINUE":
                hypothecation = continueHypothecation(vehicle, request);
                break;
                
            case "UPDATE":
                hypothecation = updateHypothecation(vehicle, request);
                break;
                
            default:
                throw new RuntimeException("Invalid action");
        }
        
        return hypothecationRepository.save(hypothecation);
    }
    
    
    
    @Autowired
    private RegistrationCertificateRepository registrationCertificateRepository;

    private Hypothecation createNewHypothecation(Vehicle vehicle, HypothecationRequest request) 
    {
    	
    	 // 🔹 Get RC for vehicle
        RegistrationCertificate rc = registrationCertificateRepository
                .findByVehicle(vehicle)
                .orElseThrow(() -> new RuntimeException("RC not found for vehicle"));

        
     // ✅ DEBUG PRINT HERE
        System.out.println("Loan Start Date: " + request.getLoanStartDate());
        System.out.println("RC Issue Date: " + rc.getIssueDate());
        
     
        // 🔹 VALIDATION: loanStartDate must match RC issueDate
        if (request.getLoanStartDate() == null) {
            throw new RuntimeException("Loan start date is required");
        }

        if (!isSameDate(request.getLoanStartDate(), rc.getIssueDate())) {
            throw new RuntimeException(
                    "Loan start date must match RC issue date: " + rc.getIssueDate()
            );
        }

             

    	
     // 🔹 Create Hypothecation
        Hypothecation hypothecation = new Hypothecation();
        hypothecation.setVehicle(vehicle);
        hypothecation.setFinancierName(request.getFinancierName());
        hypothecation.setFinancierAddress(request.getFinancierAddress());
        hypothecation.setFinancierContact(request.getFinancierContact());
        hypothecation.setLoanAccountNumber(request.getLoanAccountNumber());
        hypothecation.setLoanAmount(request.getLoanAmount());
        hypothecation.setLoanStartDate(request.getLoanStartDate());
        hypothecation.setLoanEndDate(request.getLoanEndDate());
        hypothecation.setHypothecationStatus("ACTIVE");
        hypothecation.setRemarks(request.getRemarks());
        
        return hypothecation;
    }
    
    
    private boolean isSameDate(Date date1, Date date2) {
        return date1.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .isEqual(
                        date2.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                );
    }


    
    
    
    private Hypothecation terminateHypothecation(Vehicle vehicle, HypothecationRequest request) {
        Hypothecation hypothecation = hypothecationRepository
            .findByVehicleRegistrationNumberAndHypothecationStatus(vehicle.getRegistrationNumber(), "ACTIVE")
            .orElseThrow(() -> new RuntimeException("No active hypothecation found"));
        
        hypothecation.setHypothecationStatus("TERMINATED");
        hypothecation.setRemarks(request.getRemarks());
        hypothecation.setUpdatedAt(new Date());
        
        return hypothecation;
    }
    
    private Hypothecation continueHypothecation(Vehicle vehicle, HypothecationRequest request) {
        Hypothecation hypothecation = hypothecationRepository
            .findByVehicleRegistrationNumberAndHypothecationStatus(vehicle.getRegistrationNumber(), "ACTIVE")
            .orElseThrow(() -> new RuntimeException("No active hypothecation found"));
        
        // Extend loan end date
        if (request.getLoanEndDate() != null) {
            hypothecation.setLoanEndDate(request.getLoanEndDate());
        }
        
        hypothecation.setHypothecationStatus("ACTIVE");
        hypothecation.setRemarks("Hypothecation continued - " + request.getRemarks());
        hypothecation.setUpdatedAt(new Date());
        
        return hypothecation;
    }
    
    private Hypothecation updateHypothecation(Vehicle vehicle, HypothecationRequest request) {
        Hypothecation hypothecation = hypothecationRepository
            .findByVehicleRegistrationNumberAndHypothecationStatus(vehicle.getRegistrationNumber(), "ACTIVE")
            .orElseThrow(() -> new RuntimeException("No active hypothecation found"));
        
        if (request.getFinancierName() != null) {
            hypothecation.setFinancierName(request.getFinancierName());
        }
        
        if (request.getFinancierAddress() != null) {
            hypothecation.setFinancierAddress(request.getFinancierAddress());
        }
        
        if (request.getFinancierContact() != null) {
            hypothecation.setFinancierContact(request.getFinancierContact());
        }
        
        if (request.getLoanAccountNumber() != null) {
            hypothecation.setLoanAccountNumber(request.getLoanAccountNumber());
        }
        
        if (request.getLoanAmount() != null) {
            hypothecation.setLoanAmount(request.getLoanAmount());
        }
        
        hypothecation.setRemarks("Updated - " + request.getRemarks());
        hypothecation.setUpdatedAt(new Date());
        
        return hypothecation;
    }
    
    public String generateNoc(String registrationNumber) {
        Hypothecation hypothecation = hypothecationRepository
            .findByVehicleRegistrationNumberAndHypothecationStatus(registrationNumber, "ACTIVE")
            .orElseThrow(() -> new RuntimeException("No active hypothecation found"));
        
        // Generate NOC
        String nocToken = "NOC-" + UUID.randomUUID().toString().toUpperCase();
        String nocUrl = "https://noc.ahan.gov.in/" + nocToken;
        
        hypothecation.setNocIssued(true);
        hypothecation.setNocIssueDate(new Date());
        hypothecation.setDigitalNocToken(nocToken);
        hypothecation.setNocDocumentUrl(nocUrl);
        hypothecationRepository.save(hypothecation);
        
        return nocUrl;
    }
    
    public List<Hypothecation> getHypothecationHistory(String registrationNumber) {
        return hypothecationRepository.findByVehicleRegistrationNumber(registrationNumber);
    }
}