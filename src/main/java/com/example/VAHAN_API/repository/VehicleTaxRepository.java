package com.example.VAHAN_API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.VAHAN_API.entity.VehicleTax;

public interface VehicleTaxRepository extends JpaRepository<VehicleTax, Long> {

    Optional<VehicleTax> findByReceiptNumber(String receiptNumber);

    List<VehicleTax> findByRegistrationNumber(String registrationNumber);
    
    
    // validation for tax pay
    Optional<VehicleTax> 
    findTopByRegistrationNumberAndTaxTypeAndTaxPeriodOrderByPaymentDateDesc(
        String registrationNumber,
        String taxType,
        String taxPeriod
    );
    
    Optional<VehicleTax>
    findTopByRegistrationNumberAndStatusOrderByPaymentDateDesc(
            String registrationNumber,
            String status
    );


    
    
}
