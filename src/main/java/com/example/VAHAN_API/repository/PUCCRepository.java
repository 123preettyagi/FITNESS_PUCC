package com.example.VAHAN_API.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.VAHAN_API.entity.PUCCCertificate;

@Repository    
	public interface PUCCRepository extends JpaRepository<PUCCCertificate, Long> {

	    Optional<PUCCCertificate> findTopByRegistrationNumberOrderByIssueDateDesc(String registrationNumber);
	}
