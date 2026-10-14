package com.example.VAHAN_API.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.VAHAN_API.dto.PUCCRequest;
import com.example.VAHAN_API.dto.PUCCResponse;
import com.example.VAHAN_API.entity.PUCCCertificate;
import com.example.VAHAN_API.entity.RegistrationCertificate;
import com.example.VAHAN_API.entity.Vehicle;
//import com.example.VAHAN_API.entity.Vehicle;
import com.example.VAHAN_API.repository.PUCCRepository;
import com.example.VAHAN_API.repository.RegistrationCertificateRepository;
import com.example.VAHAN_API.repository.VehicleRepository;


@Service
public class PUCCService {

    private final PUCCRepository puccRepository;
    private final VehicleRepository vehicleRepository;
    private final  RegistrationCertificateRepository rcRepository;  
   

    //  Constructor Injection
    public PUCCService(PUCCRepository puccRepository,VehicleRepository vehicleRepository,RegistrationCertificateRepository rcRepository) 
    {
        this.puccRepository = puccRepository;
        this.vehicleRepository = vehicleRepository;
        this.rcRepository=rcRepository;
    }

    // 1️⃣ ISSUE PUCC
    public PUCCCertificate issuePUCC(PUCCRequest request) 
    {

        
    	//  Vehicle must exist
        Vehicle vehicle = vehicleRepository   
                .findByRegistrationNumber(request.getRegistrationNumber())
                .orElseThrow(() ->
                        new RuntimeException
                        (
                                "Vehicle not found with registration number: "
                                        + request.getRegistrationNumber()
                        )
                );

            
        // RC	
    	RegistrationCertificate rc = rcRepository
    	        .findByVehicleRegistrationNumber(request.getRegistrationNumber())
    	        .orElseThrow(() -> new RuntimeException("Vehicle  found but RC NOT found"));
        
        
        
     //  Check last PUCC based on ISSUE DATE, not current date
        puccRepository
            .findTopByRegistrationNumberOrderByIssueDateDesc(
                    request.getRegistrationNumber()
            )
            .ifPresent(existing -> {

                LocalDate newIssueDate = request.getIssueDate();

                // ❌ Overlapping PUCC not allowed
                if (!newIssueDate.isAfter(existing.getExpiryDate())) {
                    throw new RuntimeException(
                        "PUCC already exists and is valid till "
                                + existing.getExpiryDate()
                    );
                }
            });

        
        
        
        
     //  Calculate expiry date (correct logic) 
     // VALIDATION: PUCC validity must be 6 months
        if (request.getValidityMonths() != 6) 
        {
            throw new RuntimeException("PUCC validity must be exactly 6 months");
        }
        
        YearMonth expiryMonth = YearMonth.from(request.getIssueDate())
                .plusMonths(request.getValidityMonths());

        LocalDate expiryDate = expiryMonth
                .minusMonths(1)
                .atEndOfMonth();
        
        
        
     // Issue new PUCC
        
        PUCCCertificate pucc = new PUCCCertificate();
        pucc.setRegistrationNumber(request.getRegistrationNumber());
        pucc.setIssueDate(request.getIssueDate());
        pucc.setExpiryDate(expiryDate);
        pucc.setTestingCenter(request.getTestingCenter());
        pucc.setPuccNumber("PUCC-" + UUID.randomUUID());
        pucc.setCoPercentage(request.getCoPercentage());
        pucc.setHcPpm(request.getHcPpm());
        
        
        return puccRepository.save(pucc);
    }
    
    

    // 2️⃣ FETCH PUCC
    public PUCCResponse fetchPUCC(String registrationNumber) 
    {

        PUCCCertificate pucc = puccRepository
                .findTopByRegistrationNumberOrderByIssueDateDesc(registrationNumber)
                .orElseThrow(() -> new RuntimeException("PUCC not found"));

        return new PUCCResponse(
                pucc.getPuccNumber(),
                pucc.getIssueDate(),
                pucc.getExpiryDate(),
                pucc.getCoPercentage(),
                pucc.getHcPpm()
        );
    }

     
    
    
    // 3 EXPIRY CHECK
    public boolean isPUCCValid(String registrationNumber) 
    {

        PUCCCertificate pucc = puccRepository
                .findTopByRegistrationNumberOrderByIssueDateDesc(registrationNumber)
                .orElseThrow(() -> new RuntimeException("PUCC not found"));

        return !LocalDate.now().isAfter(pucc.getExpiryDate());
    }
    
    
    
    
    
    
 // 4️⃣ RENEW PUCC 
    public PUCCCertificate renewPUCC(String registrationNumber, String testingCenter) 
    {

        // Vehicle must exist
        vehicleRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() ->
                        new RuntimeException("Vehicle not found: " + registrationNumber));

        // Fetch latest PUCC
        PUCCCertificate lastPUCC = puccRepository
                .findTopByRegistrationNumberOrderByIssueDateDesc(registrationNumber)
                .orElseThrow(() -> new RuntimeException("No PUCC found to renew"));

        // Must be expired
        if (!LocalDate.now().isAfter(lastPUCC.getExpiryDate())) {
            throw new RuntimeException(
                    "PUCC is still valid till " + lastPUCC.getExpiryDate());
        }

        // New dates
        LocalDate newIssueDate = LocalDate.now();
        LocalDate newExpiryDate = YearMonth.from(newIssueDate)
                .plusMonths(6)
                .minusMonths(1)
                .atEndOfMonth();

        // 🔥 UPDATE SAME RECORD
        lastPUCC.setIssueDate(newIssueDate);
        lastPUCC.setExpiryDate(newExpiryDate);
        lastPUCC.setTestingCenter(testingCenter);

        return puccRepository.save(lastPUCC);
    }

    
         
         
         
    
}