package com.example.VAHAN_API.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.VAHAN_API.dto.PermitRequest;
import com.example.VAHAN_API.entity.Permit;
import com.example.VAHAN_API.entity.RegistrationCertificate;
import com.example.VAHAN_API.repository.PermitRepository;
import com.example.VAHAN_API.repository.RegistrationCertificateRepository;

@Service
public class PermitService {

    private final PermitRepository permitRepository;
    private final RegistrationCertificateRepository rcRepository;
    private final InsuranceService insuranceService; 
    

    //  Manual Constructor Injection (Instead of @RequiredArgsConstructor)
    public PermitService(PermitRepository permitRepository,
                         RegistrationCertificateRepository rcRepository,InsuranceService insuranceService) 
    {
        this.permitRepository = permitRepository;
        this.rcRepository = rcRepository;
        this.insuranceService = insuranceService;
    }

    // ISSUE PERMIT
    public Permit issuePermit(PermitRequest request) {

        //  CHECK RC EXISTS
    	 RegistrationCertificate rc = rcRepository
    	            .findByVehicleRegistrationNumber(request.getRegistrationNumber())
    	            .orElseThrow(() ->
    	                    new RuntimeException("RC not found. Permit cannot be issued."));

        //  CHECK RC STATUS
    	 if (!rc.getRcStatus().equalsIgnoreCase("ACTIVE")) {
    	        throw new RuntimeException("RC is not ACTIVE. Permit cannot be issued.");
    	    }
    	 
    	//  CHECK INSURANCE VALIDITY  👈 NEW LOGIC
    	    boolean insuranceValid =
    	            insuranceService.isInsuranceValid(request.getRegistrationNumber());

    	    if (!insuranceValid) {
    	        throw new RuntimeException(
    	                "Insurance is expired or not active. Permit cannot be issued.");
    	    }
    	 
    	 
    	 

        // 3 CREATE PERMIT
    	 Permit permit = new Permit();
    	    permit.setRegistrationNumber(request.getRegistrationNumber());
    	    permit.setPermitType(request.getPermitType());
    	    permit.setIssueDate(request.getIssueDate());
    	    permit.setExpiryDate(request.getExpiryDate());
    	    permit.setIssuingAuthority(request.getIssuingAuthority());
    	    permit.setStatus("ACTIVE");

    	    return permitRepository.save(permit);
    	 
    	 
    	 
    }

    // RENEW PERMIT
    public Permit renewPermit(Long permitId) {

        Permit permit = permitRepository.findById(permitId)
                .orElseThrow(() -> new RuntimeException("Permit not found"));

        permit.setExpiryDate(permit.getExpiryDate().plusYears(1));
        permit.setStatus("ACTIVE");

        return permitRepository.save(permit);
    }

    // GET PERMIT HISTORY
    public List<Permit> getPermitHistory(String registrationNumber) {
        return permitRepository.findByRegistrationNumber(registrationNumber);
    }

    // CANCEL PERMIT
    public Permit cancelPermit(Long permitId) {

        Permit permit = permitRepository.findById(permitId)
                .orElseThrow(() -> new RuntimeException("Permit not found"));

        permit.setStatus("CANCELLED");

        return permitRepository.save(permit);
    }
}
