package com.example.VAHAN_API.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.VAHAN_API.entity.OwnershipTransfer;

@Repository
public interface OwnershipTransferRepository extends JpaRepository<OwnershipTransfer, Long> 
{
    List<OwnershipTransfer> findByVehicleRegistrationNumber(String registrationNumber);
    
}