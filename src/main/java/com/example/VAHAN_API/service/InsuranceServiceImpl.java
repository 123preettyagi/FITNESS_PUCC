package com.example.VAHAN_API.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.VAHAN_API.dto.InsuranceRequest;
import com.example.VAHAN_API.entity.FitnessCertificate;
import com.example.VAHAN_API.entity.Insurance;
import com.example.VAHAN_API.entity.RegistrationCertificate;
import com.example.VAHAN_API.entity.Vehicle;
import com.example.VAHAN_API.repository.FitnessCertificateRepository;
import com.example.VAHAN_API.repository.InsuranceRepository;
import com.example.VAHAN_API.repository.RegistrationCertificateRepository;
import com.example.VAHAN_API.repository.VehicleRepository;

@Service
public class InsuranceServiceImpl implements InsuranceService {

    @Autowired
    private InsuranceRepository insuranceRepository;
     
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private RegistrationCertificateRepository rcRepository; 
    
    
    @Autowired
    private FitnessCertificateRepository fitnessRepository;

    @Override
    public Insurance linkInsurance(InsuranceRequest request) 
    {

        // 1️⃣ Check if vehicle exists
        Vehicle vehicle = vehicleRepository.findByRegistrationNumber(request.getRegistrationNumber())
                .orElseThrow(() -> new RuntimeException(
                        "Vehicle with registration number " + request.getRegistrationNumber() + " does not exist"));

     
        
        
     // RC	
    	RegistrationCertificate rc = rcRepository
    	        .findByVehicleRegistrationNumber(request.getRegistrationNumber())
    	        .orElseThrow(() -> new RuntimeException("Vehicle  found but RC NOT found"));
        
    	
    	
    	
    	//  Check Fitness Certificate exists
        FitnessCertificate fitness = fitnessRepository
                .findByRegistrationNumber(request.getRegistrationNumber())
                .orElseThrow(() -> new RuntimeException("Fitness Certificate not found"));

        //  Check Fitness validity
        if (fitness.getExpiryDate().before(new java.util.Date())) {
            throw new RuntimeException("Fitness Certificate expired. Cannot issue insurance.");
        }
        
    	
        
        //  Check if insurance already exists and active
        insuranceRepository.findTopByRegistrationNumberOrderByEndDateDesc(request.getRegistrationNumber())
                .ifPresent(existingInsurance -> {
                    if (existingInsurance.getEndDate().isAfter(LocalDate.now())) {
                        throw new RuntimeException(
                                "Active insurance already exists for this vehicle: " + existingInsurance.getPolicyNumber());
                    }
                });

        //  Save new insurance
        Insurance insurance = new Insurance();
        insurance.setRegistrationNumber(request.getRegistrationNumber());
        insurance.setPolicyNumber(request.getPolicyNumber());
        insurance.setInsurerName(request.getInsurerName());
        insurance.setStartDate(request.getStartDate());
        insurance.setEndDate(request.getEndDate());
        insurance.setStatus("ACTIVE");

        return insuranceRepository.save(insurance);
    }

    
    

    @Override
    public Insurance verifyInsurance(String registrationNumber) {
        return insuranceRepository
                .findTopByRegistrationNumberOrderByEndDateDesc(registrationNumber)
                .orElseThrow(() -> new RuntimeException("No insurance found"));
    }

    @Override
    public boolean isInsuranceValid(String registrationNumber)
    {
        Insurance insurance = verifyInsurance(registrationNumber);
        return insurance.getEndDate().isAfter(LocalDate.now());
    }

    
    
    @Override
    public Insurance verifyAccidentClaim(String policyNumber, Boolean injury) {

        Insurance insurance = insuranceRepository.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        if (!insurance.getEndDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Insurance expired, claim rejected");
        }

        insurance.setInjury(injury);

        if (injury) {
            insurance.setStatus("CLAIMED");
        } else {
            throw new RuntimeException("No injury reported, claim not allowed");
        }

        return insuranceRepository.save(insurance);
    }

    

    @Override
    public List<Insurance> getInsuranceHistory(String registrationNumber) 
    {
        return insuranceRepository.findByRegistrationNumber(registrationNumber);
    }
}
