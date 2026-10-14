package com.example.VAHAN_API.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.VAHAN_API.entity.FitnessCertificate;

public interface FitnessCertificateRepository extends JpaRepository<FitnessCertificate, Long> 
{
	
Optional<FitnessCertificate> findByRegistrationNumber(String registrationNumber);

}