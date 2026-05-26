package com.example.VAHAN_API.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.VAHAN_API.entity.RegistrationCertificate;
import com.example.VAHAN_API.entity.Vehicle;

@Repository
public interface RegistrationCertificateRepository extends JpaRepository<RegistrationCertificate, Long> 
{
    Optional<RegistrationCertificate> findByRcNumber(String rcNumber);
    Optional<RegistrationCertificate> findByVehicleRegistrationNumber(String registrationNumber);
    
    
    Optional<RegistrationCertificate> findByVehicle(Vehicle vehicle);

    
}