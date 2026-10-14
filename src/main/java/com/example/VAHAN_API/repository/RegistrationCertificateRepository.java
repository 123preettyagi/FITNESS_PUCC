package com.example.VAHAN_API.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.VAHAN_API.entity.RegistrationCertificate;

 @Repository
public interface RegistrationCertificateRepository extends JpaRepository<RegistrationCertificate, Long> 
{
 
	    Optional<RegistrationCertificate> findByRegistrationNumber(String registrationNumber);

	    //  Fetch all RCs that are ACTIVE but whose expiry date has passed
	    List<RegistrationCertificate> findByRegistrationStatusAndRegistrationExpiryDateBefore(
	            String registrationStatus, Date currentDate);
	    

	    


@Modifying(clearAutomatically = true, flushAutomatically = true)
@Transactional
@Query(value = """
    UPDATE registration_certificates
    SET registration_status = 'INACTIVE'
    WHERE UPPER(TRIM(registration_status)) = 'ACTIVE'
      AND registration_expiry_date <= CURRENT_DATE
    """, nativeQuery = true)
int updateExpiredRcStatus();

    
}  

