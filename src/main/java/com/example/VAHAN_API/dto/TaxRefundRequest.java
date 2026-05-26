package com.example.VAHAN_API.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaxRefundRequest {

	
	 @NotBlank(message = "Receipt number is required")
	    @Size(min = 5, max = 70, message = "Receipt number must be between 5 and 30 characters")
    private String receiptNumber;
	 
	 @NotBlank(message = "Reason is required") 
    private String reason;

    // No-args constructor (recommended for frameworks)
    public TaxRefundRequest() {
    }

    // Parameterized constructor (optional but useful)
    public TaxRefundRequest(String receiptNumber, String reason) {
        this.receiptNumber = receiptNumber;
        this.reason = reason;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
