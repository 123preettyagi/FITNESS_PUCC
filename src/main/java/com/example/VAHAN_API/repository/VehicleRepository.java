package com.example.VAHAN_API.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.VAHAN_API.entity.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>
{
	
    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);
    Optional<Vehicle> findByChassisNumber(String chassisNumber);
    Optional<Vehicle> findByEngineNumber(String engineNumber);
    
    
    // Add this method FOR FITNESS 
    boolean existsByRegistrationNumber(String registrationNumber);
    
}