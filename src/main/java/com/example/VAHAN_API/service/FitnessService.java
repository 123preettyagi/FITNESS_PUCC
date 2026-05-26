package com.example.VAHAN_API.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;
import com.example.VAHAN_API.entity.FitnessCertificate;
import com.example.VAHAN_API.entity.PUCCCertificate;
import com.example.VAHAN_API.entity.RegistrationCertificate;
import com.example.VAHAN_API.entity.Vehicle;
import com.example.VAHAN_API.repository.FitnessCertificateRepository;
import com.example.VAHAN_API.repository.PUCCRepository;
import com.example.VAHAN_API.repository.RegistrationCertificateRepository;
import com.example.VAHAN_API.repository.VehicleRepository;

@Service
public class FitnessService {


private final FitnessCertificateRepository fitnessRepo;
private final VehicleRepository vehicleRepo;
private final  RegistrationCertificateRepository rcRepository; 
private final PUCCRepository puccrepo;


public FitnessService(FitnessCertificateRepository fitnessRepo, VehicleRepository vehicleRepo,RegistrationCertificateRepository rcRepository,PUCCRepository puccrepo) 
{
	
this.fitnessRepo = fitnessRepo;
this.vehicleRepo = vehicleRepo;
this.rcRepository=rcRepository;
this.puccrepo=puccrepo;

}


// 🚗 FITNESS VALIDITY RULES
// Petrol: 15 years then 5 year renewal
// Diesel: 10 years then 5 year renewal
// Electric: 15 years


private Date calculateExpiry(Vehicle vehicle, Date issueDate) 
{
Calendar cal = Calendar.getInstance();
cal.setTime(issueDate);


int years;
if (vehicle.getFuelType().equalsIgnoreCase("PETROL")) years = 15;
else if (vehicle.getFuelType().equalsIgnoreCase("DIESEL")) years = 10;
else years = 15;


cal.add(Calendar.YEAR, years);
return cal.getTime();
}


public FitnessCertificate issueFitness(String regNo, String authority) 
{

    //  Fetch vehicle
    Vehicle vehicle = vehicleRepo.findByRegistrationNumber(regNo)
            .orElseThrow(() -> new RuntimeException("Vehicle not found"));

    //  Use VEHICLE REGISTRATION DATE
    Date registrationDate = vehicle.getRegistrationDate();

    
    
    // RC	
	RegistrationCertificate rc = rcRepository
	        .findByVehicleRegistrationNumber(regNo)
	        .orElseThrow(() -> new RuntimeException("Vehicle  found but RC NOT found"));
    
    
	
	// CHECK PUCC
	PUCCCertificate pucc = puccrepo
	        .findTopByRegistrationNumberOrderByIssueDateDesc(regNo)
	        .orElseThrow(() -> new RuntimeException("Vehicle PUCC NOT FOUND"));
	
	
	
    
    //  Create fitness certificate
    FitnessCertificate fc = new FitnessCertificate();
    fc.setRegistrationNumber(regNo);
    fc.setIssuedDate(registrationDate); // ✅ NOT today
    fc.setExpiryDate(calculateExpiry(vehicle, registrationDate));
    fc.setIssuingAuthority(authority);
    fc.setStatus("VALID");

    return fitnessRepo.save(fc);
}



public FitnessCertificate renewFitness(String regNo) 
{
	
	// CHECK PUCC
		PUCCCertificate pucc = puccrepo
		        .findTopByRegistrationNumberOrderByIssueDateDesc(regNo)
		        .orElseThrow(() -> new RuntimeException("Vehicle PUCC NOT FOUND"));	
	
	
FitnessCertificate fc = fitnessRepo.findByRegistrationNumber(regNo)
.orElseThrow(() -> new RuntimeException("Fitness not found"));



//Take existing expiry date
Calendar cal = Calendar.getInstance();
cal.setTime(fc.getExpiryDate());
//cal.setTime(new Date());

//INCREASE BY 5 YEAR
cal.add(Calendar.YEAR, 5);


//fc.setIssuedDate(new Date());
fc.setExpiryDate(cal.getTime());
fc.setStatus("VALID");


return fitnessRepo.save(fc);
}


 public String getStatus(String regNo) {
FitnessCertificate fc = fitnessRepo.findByRegistrationNumber(regNo)
.orElseThrow(() -> new RuntimeException("Fitness not found"));


return fc.getExpiryDate().before(new Date()) ? "EXPIRED" : "VALID";


} 



 public FitnessCertificate updateFitness(
	        String registrationNumber,
	        Date issuedDate,
	        Date expiryDate,
	        String authority,
	        String status) {

	    FitnessCertificate fc = fitnessRepo
	            .findByRegistrationNumber(registrationNumber)
	            .orElseThrow(() -> new RuntimeException("Fitness certificate not found"));

	    // Update values
	    fc.setIssuedDate(issuedDate);
	    fc.setExpiryDate(expiryDate);
	    fc.setIssuingAuthority(authority);
	    fc.setStatus(status.toUpperCase());

	    return fitnessRepo.save(fc);
	}




}