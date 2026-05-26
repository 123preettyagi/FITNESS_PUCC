package com.example.VAHAN_API.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    
    public RegistrationCertificate issueRc(RcRequest request) 
    {
    	// Fetch the vehicle first
        Vehicle vehicle = vehicleRepository.findByRegistrationNumber(request.getRegistrationNumber())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        
        
        
     // Check if an RC already exists for this vehicle
        rcRepository.findByVehicleRegistrationNumber(request.getRegistrationNumber())
                .ifPresent(rc -> {
                    throw new RuntimeException("RC already issued for this vehicle: " + rc.getRcNumber());
                });
        
     // If not, issue a new RC
        RegistrationCertificate rc = new RegistrationCertificate();
        rc.setRcNumber(generateRcNumber());
        rc.setVehicle(vehicle);
        rc.setOwnerName(request.getOwnerName());
        rc.setFatherHusbandName(request.getFatherHusbandName());
        rc.setPermanentAddress(request.getPermanentAddress());
        rc.setTemporaryAddress(request.getTemporaryAddress());
        rc.setMobileNumber(request.getMobileNumber());
        rc.setEmail(request.getEmail());
        rc.setIssueDate(new Date());
        rc.setExpiryDate(calculateRcExpiryDate());
        rc.setRcStatus("ACTIVE");
        rc.setRcType(request.getRcType());
        
        return rcRepository.save(rc);
    }
    
    public RegistrationCertificate renewRc(String rcNumber) 
    {
        RegistrationCertificate rc = rcRepository.findByRcNumber(rcNumber)
                .orElseThrow(() -> new RuntimeException("RC not found"));
        
        rc.setExpiryDate(calculateRcExpiryDate());
        rc.setRcStatus("ACTIVE");
        rc.setUpdatedAt(new Date());
        
        return rcRepository.save(rc);
    }
    
    public RegistrationCertificate issueDuplicateRc(String rcNumber) {
        RegistrationCertificate originalRc = rcRepository.findByRcNumber(rcNumber)
                .orElseThrow(() -> new RuntimeException("RC not found"));
        
        RegistrationCertificate duplicateRc = new RegistrationCertificate();
        duplicateRc.setRcNumber("DUP-" + originalRc.getRcNumber());
        duplicateRc.setVehicle(originalRc.getVehicle());
        duplicateRc.setOwnerName(originalRc.getOwnerName());
        duplicateRc.setFatherHusbandName(originalRc.getFatherHusbandName());
        duplicateRc.setPermanentAddress(originalRc.getPermanentAddress());
        duplicateRc.setTemporaryAddress(originalRc.getTemporaryAddress());
        duplicateRc.setMobileNumber(originalRc.getMobileNumber());
        duplicateRc.setEmail(originalRc.getEmail());
        duplicateRc.setIssueDate(new Date());
        duplicateRc.setExpiryDate(originalRc.getExpiryDate());
        duplicateRc.setRcStatus("ACTIVE");
        duplicateRc.setRcType("DUPLICATE");
        
        return rcRepository.save(duplicateRc);
    } 
    
    
   

    
    public RegistrationCertificate cancelRc(String rcNumber) {
        RegistrationCertificate rc = rcRepository.findByRcNumber(rcNumber)
                .orElseThrow(() -> new RuntimeException("RC not found"));
        
        rc.setRcStatus("CANCELLED");
        rc.setUpdatedAt(new Date());
        
        return rcRepository.save(rc);
    }
    
    public String getDigitalRc(String rcNumber) {
        RegistrationCertificate rc = rcRepository.findByRcNumber(rcNumber)
                .orElseThrow(() -> new RuntimeException("RC not found"));
        
        // Generate digital RC URL (simulated)
        String digitalUrl = "https://digilocker.gov.in/rc/" + rcNumber + "/" + UUID.randomUUID().toString();
        rc.setDigitalRcUrl(digitalUrl);
        rc.setIsDigitalCopyAvailable(true);
        rcRepository.save(rc);
        
        return digitalUrl;
    }
    
    private String generateRcNumber() {
        return "RC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase() + 
               "-" + System.currentTimeMillis() % 10000;
    }
    
    private Date calculateRcExpiryDate() {
        Date date = new Date();
        // 15 years from now
        return new Date(date.getTime() + (15L * 365 * 24 * 60 * 60 * 1000));
    }
    
    
    public RegistrationCertificate getRcByNumber(String rcNumber) {
        return rcRepository.findByRcNumber(rcNumber)
                .orElseThrow(() ->
                        new RuntimeException("RC not found with number: " + rcNumber));
    }
    
    // ADDRESS CHANGE
    public RegistrationCertificate updateAddress(String rcNumber, String newAddress) 
    {

        RegistrationCertificate rc = rcRepository.findByRcNumber(rcNumber)
                .orElseThrow(() -> new RuntimeException("RC not found"));

        // choose one or both depending on your rule
        rc.setPermanentAddress(newAddress);
        rc.setTemporaryAddress(newAddress);

        rc.setUpdatedAt(new Date());

        return rcRepository.save(rc); 
    }
    
    // CORRECT NAME
    public RegistrationCertificate updateOwnerName(String rcNumber, String correctName) {

        RegistrationCertificate rc = rcRepository.findByRcNumber(rcNumber)
                .orElseThrow(() -> new RuntimeException("RC not found"));

        rc.setOwnerName(correctName);
        rc.setUpdatedAt(new Date());

        return rcRepository.save(rc);
    }

    
    
    
}