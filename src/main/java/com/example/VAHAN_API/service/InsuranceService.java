package com.example.VAHAN_API.service;

import java.util.List;

import com.example.VAHAN_API.dto.InsuranceRequest;
import com.example.VAHAN_API.entity.Insurance;

public interface InsuranceService {

    Insurance linkInsurance(InsuranceRequest request);

    Insurance verifyInsurance(String registrationNumber);

    boolean isInsuranceValid(String registrationNumber);

    //Insurance verifyAccidentClaim(String policyNumber);
    
    Insurance verifyAccidentClaim(String policyNumber, Boolean injury);

    List<Insurance> getInsuranceHistory(String registrationNumber);
    
}