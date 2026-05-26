package com.example.VAHAN_API.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.VAHAN_API.dto.OwnershipTransferRequest;
import com.example.VAHAN_API.entity.OwnershipTransfer;
import com.example.VAHAN_API.entity.RegistrationCertificate;
import com.example.VAHAN_API.entity.Vehicle;
import com.example.VAHAN_API.repository.OwnershipTransferRepository;
import com.example.VAHAN_API.repository.RegistrationCertificateRepository;
import com.example.VAHAN_API.repository.VehicleRepository;

@Service
public class OwnershipTransferService {
    
    @Autowired
    private OwnershipTransferRepository transferRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private RegistrationCertificateRepository rcRepository;
    
    public OwnershipTransfer transferOwnership(OwnershipTransferRequest request) 
    {
    	// CHECK VEHICLE EXIST OR NOT
        Vehicle vehicle = vehicleRepository.findByRegistrationNumber(request.getRegistrationNumber())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        
        // Get current owner from RC
        RegistrationCertificate currentRc = rcRepository.findByVehicleRegistrationNumber(request.getRegistrationNumber())
                .orElseThrow(() -> new RuntimeException("RC not found"));
        
         
        
        
        OwnershipTransfer transfer = new OwnershipTransfer();
        transfer.setVehicle(vehicle);
        transfer.setTransferType(request.getTransferType());
        transfer.setPreviousOwnerName(currentRc.getOwnerName());
        transfer.setNewOwnerName(request.getNewOwnerName());
        transfer.setNewOwnerFatherName(request.getNewOwnerFatherName());
        transfer.setNewOwnerAddress(request.getNewOwnerAddress());
        transfer.setNewOwnerMobile(request.getNewOwnerMobile());
        transfer.setNewOwnerEmail(request.getNewOwnerEmail());
        transfer.setTransferDate(request.getTransferDate());
        transfer.setSaleAmount(request.getSaleAmount());
        transfer.setSaleDeedNumber(request.getSaleDeedNumber());
        transfer.setDeathCertificateNumber(request.getDeathCertificateNumber());
        transfer.setAuctionReference(request.getAuctionReference());
        transfer.setIsInterState(request.getIsInterState());
        transfer.setFromState(request.getFromState());
        transfer.setToState(request.getToState());
        transfer.setRemarks(request.getRemarks());
        transfer.setTransferStatus("PENDING");
        
        // Update RC with new owner details
        updateRcWithNewOwner(currentRc, request);
        
        return transferRepository.save(transfer);
    } 
    
    
  /* public OwnershipTransfer transferInterStateOwnership(OwnershipTransferRequest request) {

        Vehicle vehicle = vehicleRepository
                .findByRegistrationNumber(request.getRegistrationNumber())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        RegistrationCertificate rc = rcRepository
                .findByVehicleRegistrationNumber(request.getRegistrationNumber())
                .orElseThrow(() -> new RuntimeException("RC not found"));

        // ✅ Interstate validation
        if (request.getFromState() == null || request.getToState() == null) {
            throw new RuntimeException("From State and To State are required");
        }

        if (request.getFromState().equalsIgnoreCase(request.getToState())) {
            throw new RuntimeException("From State and To State cannot be same");
        }

        // ✅ Create Transfer
        OwnershipTransfer transfer = new OwnershipTransfer();
        transfer.setVehicle(vehicle);
        transfer.setTransferType("INTER_STATE");
        transfer.setPreviousOwnerName(rc.getOwnerName());
        transfer.setNewOwnerName(request.getNewOwnerName());
        transfer.setTransferDate(request.getTransferDate());
        transfer.setIsInterState(true);
        transfer.setFromState(request.getFromState());
        transfer.setToState(request.getToState());
        transfer.setTransferStatus("PENDING");

        // ✅ Save transfer first
        OwnershipTransfer savedTransfer = transferRepository.save(transfer);

        // ✅ Update RC separately
        rc.setOwnerName(request.getNewOwnerName());
        rc.setUpdatedAt(new java.util.Date());
        rcRepository.save(rc);

      //  return savedTransfer;
        return transferOwnership(request);

        
        
    }  */
    
    
    
    public OwnershipTransfer transferInterStateOwnership(
            OwnershipTransferRequest request) {

        Vehicle vehicle = vehicleRepository
                .findByRegistrationNumber(request.getRegistrationNumber())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        RegistrationCertificate rc = rcRepository
                .findByVehicleRegistrationNumber(request.getRegistrationNumber())
                .orElseThrow(() -> new RuntimeException("RC not found"));

        if (request.getFromState().equalsIgnoreCase(request.getToState())) {
            throw new RuntimeException("From State and To State cannot be same");
        }

        OwnershipTransfer transfer = new OwnershipTransfer();
        transfer.setVehicle(vehicle);
        transfer.setTransferType("INTER_STATE");
        transfer.setPreviousOwnerName(rc.getOwnerName());
        transfer.setNewOwnerName(request.getNewOwnerName());
        transfer.setTransferDate(request.getTransferDate());
        transfer.setIsInterState(true);
        transfer.setFromState(request.getFromState());
        transfer.setToState(request.getToState());
        transfer.setTransferStatus("PENDING");

        OwnershipTransfer savedTransfer = transferRepository.save(transfer);

        // Update RC
        rc.setOwnerName(request.getNewOwnerName());
        rc.setUpdatedAt(new java.util.Date());
        rcRepository.save(rc);

        return savedTransfer;   // ✅ FIXED
    }


    
    private void updateRcWithNewOwner(RegistrationCertificate rc, OwnershipTransferRequest request) {
        rc.setOwnerName(request.getNewOwnerName());
        rc.setFatherHusbandName(request.getNewOwnerFatherName());
        rc.setPermanentAddress(request.getNewOwnerAddress());
        rc.setMobileNumber(request.getNewOwnerMobile());
        rc.setEmail(request.getNewOwnerEmail());
        rc.setUpdatedAt(new java.util.Date());
        rcRepository.save(rc);
    }
    
    public OwnershipTransfer approveTransfer(Long transferId) {
        OwnershipTransfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new RuntimeException("Transfer not found"));
        
        transfer.setTransferStatus("APPROVED");
        transfer.setUpdatedAt(new java.util.Date());
        
        return transferRepository.save(transfer);
    }
    
    public List<OwnershipTransfer> getTransferHistory(String registrationNumber) {
        return transferRepository.findByVehicleRegistrationNumber(registrationNumber);
    }
}