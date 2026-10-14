package com.example.VAHAN_API.util;

public class TaxAlreadyPaidException extends RuntimeException {
    public TaxAlreadyPaidException(String message) {
        super(message);
    }
}