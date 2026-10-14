package com.example.VAHAN_API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.VAHAN_API.entity.Hypothecation;

@Repository
public interface HypothecationRepository extends JpaRepository<Hypothecation, Long> 
{
    List<Hypothecation> findByVehicleRegistrationNumber(String registrationNumber);
    Optional<Hypothecation> findByVehicleRegistrationNumberAndHypothecationStatus(String registrationNumber, String status);
}