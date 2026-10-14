package com.example.VAHAN_API.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.VAHAN_API.entity.Permit;

public interface PermitRepository extends JpaRepository<Permit, Long> 
{

    List<Permit> findByRegistrationNumber(String registrationNumber);
}