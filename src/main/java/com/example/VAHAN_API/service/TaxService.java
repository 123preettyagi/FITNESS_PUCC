package com.example.VAHAN_API.service;

import java.util.List;

import com.example.VAHAN_API.dto.CertificateResponse;
import com.example.VAHAN_API.dto.TaxRefundRequest;
import com.example.VAHAN_API.dto.TaxRequest;
import com.example.VAHAN_API.entity.VehicleTax;

public interface TaxService 
{

    VehicleTax payTax(TaxRequest request);

    VehicleTax recalculateTax(String registrationNumber);

    VehicleTax refundTax(TaxRefundRequest request);

   // String generateTaxReceipt(String receiptNumber);
    byte[] generateTaxReceiptPdf(String receiptNumber);  // NEW method

   // String generateTaxClearanceCertificate(String registrationNumber);
    CertificateResponse generateTaxClearanceCertificate(String registrationNumber);

    List<VehicleTax> taxHistory(String registrationNumber);
}
