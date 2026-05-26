package com.example.VAHAN_API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.VAHAN_API.entity.Insurance;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> 
{

	
    Optional<Insurance> findTopByRegistrationNumberOrderByEndDateDesc(String registrationNumber);

    Optional<Insurance> findByPolicyNumber(String policyNumber);

    List<Insurance> findByRegistrationNumber(String registrationNumber);
    
}