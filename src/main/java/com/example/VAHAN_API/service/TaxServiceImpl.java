package com.example.VAHAN_API.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.VAHAN_API.dto.CertificateResponse;
import com.example.VAHAN_API.dto.TaxRefundRequest;
import com.example.VAHAN_API.dto.TaxRequest;
import com.example.VAHAN_API.entity.RegistrationCertificate;
import com.example.VAHAN_API.entity.Vehicle;
import com.example.VAHAN_API.entity.VehicleTax;
import com.example.VAHAN_API.repository.RegistrationCertificateRepository;
import com.example.VAHAN_API.repository.VehicleRepository;
import com.example.VAHAN_API.repository.VehicleTaxRepository;
import com.example.VAHAN_API.util.PdfGenerator;
import com.example.VAHAN_API.util.TaxAlreadyPaidException;

import jakarta.transaction.Transactional;

@Service
public class TaxServiceImpl implements TaxService 
{

    @Autowired
    private VehicleTaxRepository taxRepository;

    @Autowired
    private RegistrationCertificateRepository rcRepository;  
    
    @Autowired
    private VehicleRepository vehicleRepository;

    
    @Override
    public VehicleTax payTax(TaxRequest request) 
    {
    
    	//   Check Vehicle exists
        Vehicle vehicle = vehicleRepository
                .findByRegistrationNumber(request.getRegistrationNumber())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

    	
    // RC	
    	RegistrationCertificate rc = rcRepository
    	        .findByVehicleRegistrationNumber(request.getRegistrationNumber())
    	        .orElseThrow(() -> new RuntimeException("Vehicle found but RC not found"));

        // Check existing tax validity
        Optional<VehicleTax> existingTax =
                taxRepository
                .findTopByRegistrationNumberAndTaxTypeAndTaxPeriodOrderByPaymentDateDesc(
                        request.getRegistrationNumber(),
                        request.getTaxType(),
                        request.getTaxPeriod()
                );

        if (existingTax.isPresent()) {
            VehicleTax tax = existingTax.get();

            if ("PAID".equals(tax.getStatus())
                    && tax.getValidTill().isAfter(LocalDate.now())) {

                throw new TaxAlreadyPaidException(
                        "Tax already paid and valid till " + tax.getValidTill()
                );
            }
        }

        
     //  Calculate tax based on Fuel Type
        double calculatedAmount = calculateTaxByFuelType(vehicle, request.getTaxPeriod());

        
        
        // Create new tax record
        VehicleTax tax = new VehicleTax();
        tax.setRegistrationNumber(request.getRegistrationNumber());
        tax.setTaxType(request.getTaxType());
        tax.setTaxPeriod(request.getTaxPeriod());
       // tax.setAmount(request.getAmount()); 
        tax.setAmount(calculatedAmount); // AMOUNT CALCULATED HERE
        tax.setStatus("PAID");
        tax.setPaymentDate(LocalDate.now());

        if ("LIFETIME".equals(request.getTaxPeriod())) {
            tax.setValidTill(LocalDate.now().plusYears(15));
        } else {
            tax.setValidTill(LocalDate.now().plusYears(1));
        }

        tax.setReceiptNumber("TAX-" + UUID.randomUUID());

        return taxRepository.save(tax);
    }
    
    private double calculateTaxByFuelType(Vehicle vehicle, String taxPeriod) {

        String fuelType = vehicle.getFuelType();

        double baseTax;

        switch (fuelType.toUpperCase()) {

            case "PETROL":
                baseTax = 5000;
                break;

            case "DIESEL":
                baseTax = 7000;
                break;

            case "EV":
            case "ELECTRIC":
                baseTax = 2000; // EV discount
                break;

            default:
                throw new RuntimeException("Unsupported fuel type: " + fuelType);
        }

        // Lifetime tax = 15x yearly
        if ("LIFETIME".equalsIgnoreCase(taxPeriod)) {
            return baseTax * 15;
        }

        return baseTax;
    }
    
    
    

    @Override
    public VehicleTax recalculateTax(String registrationNumber)
    {
        List<VehicleTax> taxes = taxRepository.findByRegistrationNumber(registrationNumber);
        if (taxes.isEmpty()) {
            throw new RuntimeException("No tax record found");
        }
        VehicleTax latest = taxes.get(taxes.size() - 1);
        latest.setAmount(latest.getAmount() + 500); // recalculation logic
        return taxRepository.save(latest);
    }

    @Override
    public VehicleTax refundTax(TaxRefundRequest request) 
    {
        VehicleTax tax = taxRepository.findByReceiptNumber(request.getReceiptNumber())
                .orElseThrow(() -> new RuntimeException("Receipt not found"));
        tax.setStatus("REFUNDED");
        return taxRepository.save(tax);
    }

    
    @Override
    public byte[] generateTaxReceiptPdf(String receiptNumber) {

        VehicleTax tax = taxRepository.findByReceiptNumber(receiptNumber)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));

        try {
            return PdfGenerator.generateTaxReceiptPdf(tax);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate tax receipt PDF", e);
        }
    }




  
    
    @Override
    @Transactional
    public CertificateResponse generateTaxClearanceCertificate(String registrationNumber) {

        VehicleTax tax = taxRepository
                .findTopByRegistrationNumberAndStatusOrderByPaymentDateDesc(
                        registrationNumber, "PAID"
                )
                .orElseThrow(() -> new RuntimeException(
                        "No PAID tax found for TCC generation"
                ));

        //  Generate certificate details
        String certNumber = "TCC-" + UUID.randomUUID();
        LocalDate issueDate = LocalDate.now();

        //  STORE IN DATABASE
        tax.setCertificateNumber(certNumber);
        tax.setIssueDate(issueDate);
        taxRepository.save(tax);

        //  Prepare response
        CertificateResponse cert = new CertificateResponse();
        cert.setCertificateNumber(certNumber);
        cert.setRegistrationNumber(registrationNumber);
        cert.setIssueDate(issueDate);
        cert.setValidTill(issueDate.plusYears(1));
        cert.setDownloadUrl("https://vahan.gov.in/tax/tcc/" + registrationNumber);

        return cert;
    }



    @Override
    public List<VehicleTax> taxHistory(String registrationNumber) 
    {
        return taxRepository.findByRegistrationNumber(registrationNumber);
    }
    
}