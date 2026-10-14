package com.example.VAHAN_API.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.VAHAN_API.dto.ApiResponse;
import com.example.VAHAN_API.dto.CertificateResponse;
import com.example.VAHAN_API.dto.FitnessUpdateRequest;
import com.example.VAHAN_API.dto.HypothecationRequest;
import com.example.VAHAN_API.dto.HypothecationRequest.HPTContinue;
import com.example.VAHAN_API.dto.HypothecationRequest.HPTManage;
import com.example.VAHAN_API.dto.HypothecationRequest.HPTTerminate;
import com.example.VAHAN_API.dto.HypothecationRequest.HypothecationAdd;
import com.example.VAHAN_API.dto.InsuranceRequest;
import com.example.VAHAN_API.dto.OwnershipTransferRequest;
import com.example.VAHAN_API.dto.OwnershipTransferRequest.TransferAuction;
import com.example.VAHAN_API.dto.OwnershipTransferRequest.TransferDeath;
import com.example.VAHAN_API.dto.OwnershipTransferRequest.TransferFull;
import com.example.VAHAN_API.dto.OwnershipTransferRequest.TransferInterstate;
import com.example.VAHAN_API.dto.OwnershipTransferRequest.TransferSale;
import com.example.VAHAN_API.dto.PUCCRequest;
import com.example.VAHAN_API.dto.PUCCResponse;
import com.example.VAHAN_API.dto.VehicleRegistrationRequest.BHSeries;
import com.example.VAHAN_API.dto.VehicleRegistrationRequest.NewVehicle;
import com.example.VAHAN_API.dto.PermitRequest;
import com.example.VAHAN_API.dto.RcRequest;
import com.example.VAHAN_API.dto.RcRequest.IssueRc;
import com.example.VAHAN_API.dto.TaxRefundRequest;
import com.example.VAHAN_API.dto.TaxRequest;
import com.example.VAHAN_API.dto.VehicleRegistrationRequest;
import com.example.VAHAN_API.entity.FitnessCertificate;
import com.example.VAHAN_API.entity.Hypothecation;
import com.example.VAHAN_API.entity.Insurance;
import com.example.VAHAN_API.entity.OwnershipTransfer;
import com.example.VAHAN_API.entity.PUCCCertificate;
import com.example.VAHAN_API.entity.Permit;
import com.example.VAHAN_API.entity.RegistrationCertificate;
import com.example.VAHAN_API.entity.Vehicle;
import com.example.VAHAN_API.entity.VehicleTax;
import com.example.VAHAN_API.repository.RegistrationCertificateRepository;
import com.example.VAHAN_API.service.FitnessService;
import com.example.VAHAN_API.service.HypothecationService;
import com.example.VAHAN_API.service.InsuranceService;
import com.example.VAHAN_API.service.OwnershipTransferService;
import com.example.VAHAN_API.service.PUCCService;
import com.example.VAHAN_API.service.PermitService;
import com.example.VAHAN_API.service.RcPdfService;
import com.example.VAHAN_API.service.RcService;
import com.example.VAHAN_API.service.TaxService;
import com.example.VAHAN_API.service.VehicleRegistrationService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {
    
    @Autowired
    private VehicleRegistrationService vehicleRegistrationService;
    
    @Autowired
    private RcService rcService;
    
    @Autowired
    private OwnershipTransferService ownershipTransferService;
    
    @Autowired
    private HypothecationService hypothecationService;
    
   /* @Autowired
    private AuthService authService; */
    
 // ========== 1️⃣ Vehicle Registration Services ==========
      
    @PostMapping("/register/new")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> registerNewVehicle(
            @Validated(NewVehicle.class) @RequestBody VehicleRegistrationRequest request) {

        // Set registration type to NEW
        request.setRegistrationType("NEW");

        // Call the service
        Vehicle vehicle = vehicleRegistrationService.registerNewVehicle(request);

        // Return response
        return ResponseEntity.ok(
                new ApiResponse(true, "New vehicle registration initiated", vehicle)
        );
    }
    
      
    @PostMapping("/register/bh-series")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> registerBHSeriesVehicle(
            @Validated(BHSeries.class) @RequestBody VehicleRegistrationRequest request) 
    {
    	// Set registration type to PERMANENT
        request.setRegistrationType("BH_SERIES");

     // Call the service
        Vehicle vehicle = vehicleRegistrationService.registerNewVehicle(request);

     // Return response
        return ResponseEntity.ok(
                new ApiResponse(true, "BH Series registration successful", vehicle)
        );
    }

    
 // ==========  Vehicle Number Validation ==========

    @Autowired
    private RegistrationCertificateRepository rcRepository;

    @GetMapping("/validate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER') or hasRole('USER')")
    public ResponseEntity<ApiResponse> validateVehicleNumber(
            @RequestParam("vehicleNumber") String vehicleNumber) {

        String normalized = vehicleNumber == null ? "" : vehicleNumber.trim().toUpperCase();

        if (normalized.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, "Vehicle number is required", null));
        }

        boolean exists = rcRepository.findByRegistrationNumber(normalized).isPresent();

        if (exists) {
            return ResponseEntity.ok(new ApiResponse(true,
                    "Vehicle number exists in RC records",
                    Map.of("vehicleNumber", normalized, "exists", true)));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(false, "Vehicle number not found in RC records",
                    Map.of("vehicleNumber", normalized, "exists", false)));
        }
    }
    // ========== 2️⃣ RC Services ==========
    
   
    @PostMapping("/rc/issue")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> issueRc(
            @Validated(IssueRc.class) @RequestBody RcRequest request)
    {
      

        // Call the service
        RegistrationCertificate rc = rcService.issueRc(request);

        // Return response
        return ResponseEntity.ok(
                new ApiResponse(true, "RC issued successfully", rc)
        );
        
        
    }
    
    
    
    
 // ========== RC PDF PRINT (SECURE) ==========

    @Autowired
    private RcPdfService rcPdfService;  
   
    @GetMapping(value = "/rc/print")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER') or hasRole('USER')")
    public ResponseEntity<?> printRcPdf(
            @RequestParam String registrationNumber,
            @RequestParam String chassisLast5) {

        try {
            byte[] pdfBytes = rcPdfService.generateRcPdf(registrationNumber, chassisLast5);

            return ResponseEntity.ok()
                    .header("Content-Disposition",
                            "inline; filename=RC-" + registrationNumber + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (RuntimeException ex) {

            // Build a plain JSON error payload (avoid ApiResponse PDF-conversion attempt)
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", ex.getMessage());
            error.put("timestamp", LocalDateTime.now());

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    // ========== 3️⃣ Ownership Transfer Services ==========
    
    
    @PostMapping("/ownership/transfer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> transferOwnership(
            @Validated(TransferFull.class) @RequestBody OwnershipTransferRequest request) {

        OwnershipTransfer transfer =
                ownershipTransferService.transferOwnership(request);

        return ResponseEntity.ok(
                new ApiResponse(true, "Ownership transfer initiated", transfer)
        );
    }

      
    @PostMapping("/ownership/transfer-sale")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> transferOwnershipBySale(
            @Validated(TransferSale.class) @RequestBody OwnershipTransferRequest request) {

        // Set transfer type to SALE
        request.setTransferType("SALE");

        // Call the service
        OwnershipTransfer transfer = ownershipTransferService.transferOwnership(request);

        // Return response
        return ResponseEntity.ok(
                new ApiResponse(true, "Ownership transfer by sale initiated", transfer)
        );
    }

    
    
    
    @PostMapping("/ownership/transfer-death")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> transferOwnershipByDeath(@Validated(TransferDeath.class)@RequestBody OwnershipTransferRequest request) {
            
    	// Set transfer type to DEATH
            request.setTransferType("DEATH");
            
         // Call the service
            OwnershipTransfer transfer = ownershipTransferService.transferOwnership(request);
            
         // Return response
            return ResponseEntity.ok(new ApiResponse(true, "Ownership transfer by death initiated", transfer));
        
       
    }
    
    @PostMapping("/ownership/transfer-auction")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> transferOwnershipByAuction(@Validated(TransferAuction.class)@RequestBody OwnershipTransferRequest request) {
            
    	// Set transfer type to AUCTION 
            request.setTransferType("AUCTION");
            
         // Call the service
            OwnershipTransfer transfer = ownershipTransferService.transferOwnership(request);
            
         // Return response
            return ResponseEntity.ok(new ApiResponse(true, "Ownership transfer by auction initiated", transfer));
       
            
        
    }
    
  /*  @PostMapping("/ownership/transfer-interstate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> transferInterStateOwnership(@Validated(TransferInterstate.class)@RequestBody OwnershipTransferRequest request) 
    {
    	// Set transfer type to INTERSTATE
            request.setTransferType("INTER_STATE");
            request.setIsInterState(true);
            
         // Call the service
            OwnershipTransfer transfer = ownershipTransferService.transferOwnership(request);
            
         // Return response
            return ResponseEntity.ok(new ApiResponse(true, "Inter-state ownership transfer initiated", transfer));
        
          
    } */
    
    
    @PostMapping("/ownership/transfer-interstate")
    public ResponseEntity<ApiResponse> transferInterStateOwnership(
            @Validated(TransferInterstate.class)
            @RequestBody OwnershipTransferRequest request) {

        request.setTransferType("INTER_STATE");
        request.setIsInterState(true);

        OwnershipTransfer transfer =
                ownershipTransferService.transferInterStateOwnership(request);

        return ResponseEntity.ok(
                new ApiResponse(true,
                        "Inter-state ownership transfer initiated",
                        transfer)
        );
    }
 
    
    
    
    
    @PutMapping("/ownership/approve/{transferId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> approveOwnershipTransfer(@PathVariable Long transferId) {
        try {
            OwnershipTransfer transfer = ownershipTransferService.approveTransfer(transferId);
            return ResponseEntity.ok(new ApiResponse(true, "Ownership transfer approved", transfer));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    @GetMapping("/ownership/history/{registrationNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER') or hasRole('USER')")
    public ResponseEntity<ApiResponse> getOwnershipHistory(@PathVariable String registrationNumber) {
        try {
            List<OwnershipTransfer> history = ownershipTransferService.getTransferHistory(registrationNumber);
            
            if (history.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Registration number not found"));
            }
            
            
            return ResponseEntity.ok(new ApiResponse(true, "Ownership history retrieved", history));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    // ========== 4️⃣ Address & Personal Detail Services ==========
   /*       
    @PutMapping("/details/change-address")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> changeAddress(
            @RequestParam String rcNumber,
            @RequestParam String newAddress) 
    {
        try {
            RegistrationCertificate rc =
                    rcService.updateAddress(rcNumber, newAddress);

            return ResponseEntity.ok(
                new ApiResponse(
                    true,
                    "Address Updated Successfully",
                    Map.of(
                        "rcNumber", rc.getRcNumber(),
                        "permanentAddress", rc.getPermanentAddress(),
                        "temporaryAddress", rc.getTemporaryAddress()
                    )
                )
            );
        } 
        catch (Exception e) 
        {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    } */

    /*
    @PutMapping("/details/correct-name")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> correctName(
            @RequestParam String rcNumber,
            @RequestParam String correctName) 
    {

        try {
            RegistrationCertificate rc =
                    rcService.updateOwnerName(rcNumber, correctName);

            return ResponseEntity.ok(
                new ApiResponse(
                    true,
                    "Name Corrected Successfully",
                    Map.of(
                        "rcNumber", rc.getRcNumber(),
                        "ownerName", rc.getOwnerName()
                    )
                )
            );
        } 
        catch (Exception e) 
        {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
*/
    
    // ========== 5️⃣ Hypothecation Services ==========
    
    @PostMapping("/hypothecation/manage")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> manageHypothecation(@Validated(HPTManage.class)@RequestBody HypothecationRequest request) {
       
    	// Call service
            Hypothecation hypothecation = hypothecationService.manageHypothecation(request);
            
         // Return response
            return ResponseEntity.ok(new ApiResponse(true, "Hypothecation " + request.getAction() + " successful", hypothecation));
        
            
      
    }
    
  /*  @PostMapping("/hypothecation/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> addHypothecation(
            @Validated(HypothecationAdd.class) @RequestBody HypothecationRequest request) 
    {
        // Set action type
        request.setAction("ADD");

        // Call service
        Hypothecation hypothecation = hypothecationService.manageHypothecation(request);

        // Return response
        return ResponseEntity.ok(
                new ApiResponse(true, "Hypothecation added successfully", hypothecation)
        );
    } */
    
    
    @PostMapping("/hypothecation/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> addHypothecation(
            @Validated(HypothecationAdd.class) @RequestBody HypothecationRequest request) 
    {

        try {
            request.setAction("ADD");

            Hypothecation hypothecation =
                    hypothecationService.manageHypothecation(request);

            return ResponseEntity.ok(
                    new ApiResponse(true, "Hypothecation added successfully", hypothecation)
            );

        } 
        
        catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage(), null));
        }  
        
       

        
        
    }


    
    @PostMapping("/hypothecation/terminate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> terminateHypothecation(@Validated(HPTTerminate.class)@RequestBody HypothecationRequest request) {
        
    	// Set action type
            request.setAction("TERMINATE");
            
         // Call service
            Hypothecation hypothecation = hypothecationService.manageHypothecation(request);
            
         // Return response
            return ResponseEntity.ok(new ApiResponse(true, "Hypothecation terminated successfully", hypothecation));     
    }
    
    @PostMapping("/hypothecation/continue")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> continueHypothecation(@Validated(HPTContinue.class)@RequestBody HypothecationRequest request) {
        
    	  // Set action type
            request.setAction("CONTINUE");
            
         // Call service
            Hypothecation hypothecation = hypothecationService.manageHypothecation(request);
           
            // Return response
            return ResponseEntity.ok(new ApiResponse(true, "Hypothecation continued successfully", hypothecation));
       
    }
    
    @GetMapping("/hypothecation/noc/{registrationNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> generateNoc(@PathVariable String registrationNumber) {
        try {
            String nocUrl = hypothecationService.generateNoc(registrationNumber);
            return ResponseEntity.ok(new ApiResponse(true, "NOC generated successfully", nocUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    @GetMapping("/hypothecation/history/{registrationNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER') or hasRole('USER')")
    public ResponseEntity<ApiResponse> getHypothecationHistory(@PathVariable String registrationNumber) {
        try {
            List<Hypothecation> history = hypothecationService.getHypothecationHistory(registrationNumber);
            return ResponseEntity.ok(new ApiResponse(true, "Hypothecation history retrieved", history));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    
    
 // ========== 6  Permit Services (Commercial Vehicles)==========
   
    @Autowired
    private PermitService permitService;

    // 1️⃣ ISSUE PERMIT
    @PostMapping("/permit/issue")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> issuePermit(@Valid @RequestBody PermitRequest request) {

        Permit permit = permitService.issuePermit(request);

        return ResponseEntity.ok(
                new ApiResponse(true, "Permit issued successfully", permit)
        );
    }


    // 2️⃣ RENEW PERMIT
    @PutMapping("/permit/renew/{permitId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> renewPermit(@PathVariable Long permitId) {

        Permit permit = permitService.renewPermit(permitId);

        return ResponseEntity.ok(
                new ApiResponse(true, "Permit renewed successfully", permit)
        );
    }


    // 3️⃣ CANCEL PERMIT
    @PutMapping("/permit/cancel/{permitId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> cancelPermit(@PathVariable Long permitId) {

        Permit permit = permitService.cancelPermit(permitId);

        return ResponseEntity.ok(
                new ApiResponse(true, "Permit cancelled successfully", permit)
        );
    }


    // 4️⃣ PERMIT HISTORY
    @GetMapping("/permit/history/{registrationNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER') or hasRole('USER')")
    public ResponseEntity<ApiResponse> getPermitHistory(@PathVariable String registrationNumber) {

        return ResponseEntity.ok(
                new ApiResponse(true, "Permit history fetched",
                        permitService.getPermitHistory(registrationNumber))
        );
    }

    
    
    
    
    
    
    
 // ========== 7️⃣ Tax Services ==========
    @Autowired
    private TaxService taxService;

    // 1️⃣ Road / Lifetime / Green Tax Payment
    @PostMapping("/tax/pay")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> payTax(@Valid @RequestBody TaxRequest request) 
    {
        VehicleTax tax = taxService.payTax(request);
        return ResponseEntity.ok(new ApiResponse(true, "Tax paid successfully", tax));
    }

    // 2️⃣ Tax Recalculation
    @PutMapping("/tax/recalculate/{registrationNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> recalculateTax(@PathVariable String registrationNumber) {
        VehicleTax tax = taxService.recalculateTax(registrationNumber);
        return ResponseEntity.ok(new ApiResponse(true, "Tax recalculated", tax));
    }

    // 3️⃣ Tax Refund
    @PostMapping("/tax/refund")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> refundTax(@Valid @RequestBody TaxRefundRequest request) {
        VehicleTax tax = taxService.refundTax(request);
        return ResponseEntity.ok(new ApiResponse(true, "Tax refunded", tax));
    }

    // 4️⃣ Tax Clearance Certificate
        
    @GetMapping("/tax/tcc/{registrationNumber}")
    public ResponseEntity<ApiResponse> taxClearance(@PathVariable String registrationNumber) {
        CertificateResponse cert =
                taxService.generateTaxClearanceCertificate(registrationNumber);

        return ResponseEntity.ok(
            new ApiResponse(true, "Tax Clearance Certificate generated", cert)
        );
    }


    // 5️⃣ Online Tax Receipt Download  
    @GetMapping(
    	    value = "/tax/receipt/pdf/{receiptNumber}",
    	    produces = "application/pdf"
    	)
    	@PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER') or hasRole('USER')")
    	public ResponseEntity<byte[]> downloadTaxReceiptPdf(
    	        @PathVariable String receiptNumber) {

    	    byte[] pdfBytes = taxService.generateTaxReceiptPdf(receiptNumber);

    	    return ResponseEntity.ok()
    	            .header("Content-Disposition",
    	                    "attachment; filename=tax-receipt-" + receiptNumber + ".pdf")
    	            .body(pdfBytes);
    	}
    
    
    // 6️⃣ Tax History
    @GetMapping("/tax/history/{registrationNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER') or hasRole('USER')")
    public ResponseEntity<ApiResponse> taxHistory(@PathVariable String registrationNumber) {
        List<VehicleTax> history = taxService.taxHistory(registrationNumber);
        return ResponseEntity.ok(new ApiResponse(true, "Tax history retrieved", history));
    }

    
    
 // ========== 8 INSURANCE Services ==========
    
    
    
    @Autowired
    private InsuranceService insuranceService;

    // 1️ Link Insurance with RC
    
    @PostMapping("/link")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> linkInsurance(@Valid @RequestBody InsuranceRequest request) {
        try {
            Insurance insurance = insuranceService.linkInsurance(request);
            return ResponseEntity.ok(
                    new ApiResponse(true, "Insurance linked with RC successfully", insurance));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, ex.getMessage(), null));
        }
    }

    // 2️ Insurance Verification (for Police / RTO)
    @GetMapping("/verify/{registrationNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER') or hasRole('USER')")
    public ResponseEntity<ApiResponse> verifyInsurance(@PathVariable String registrationNumber) {
        Insurance insurance = insuranceService.verifyInsurance(registrationNumber);
        return ResponseEntity.ok(
                new ApiResponse(true, "Insurance verification successful", insurance));
    }

    // 3️ Insurance Validity Check
    @GetMapping("/validity/{registrationNumber}")
    public ResponseEntity<ApiResponse> checkValidity(@PathVariable String registrationNumber) {

        Insurance insurance = insuranceService.verifyInsurance(registrationNumber);

        if (insurance.getEndDate().isBefore(LocalDate.now())) {
            return ResponseEntity.ok(
                    new ApiResponse(true, "Insurance expired", null)
            );
        } 
        else {
            return ResponseEntity.ok(
                    new ApiResponse(
                            true,
                            "Insurance is valid till " + insurance.getEndDate(),
                            insurance.getEndDate()
                    )
            );
        }
    }
    
    

    //4 Accident Claim Verification (for Insurers)
    
 // VERIFY CLAIM WITH INJURY
    @PutMapping("/verify/{policyNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER') or hasRole('USER')")
    public ResponseEntity<?> verifyClaim(
            @PathVariable String policyNumber,
            @RequestParam Boolean injury) {

        return ResponseEntity.ok(
                insuranceService.verifyAccidentClaim(policyNumber, injury)
        );
    }

    
    
    

    // 5️⃣ Insurance History
    @GetMapping("/api/vehicle/insurance-history/{registrationNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER') or hasRole('USER')")
    public ResponseEntity<ApiResponse> insuranceHistory(@PathVariable String registrationNumber) {
        List<Insurance> history = insuranceService.getInsuranceHistory(registrationNumber);
        return ResponseEntity.ok(
                new ApiResponse(true, "Insurance history fetched", history));
    }
    
    
    
    
    // ========== 9 FITNESS SERVICE ========== 
    
    private final FitnessService service;


    public VehicleController(FitnessService service) 
    {
    this.service = service;
    }


    // 1️⃣ ISSUE FITNESS
    @PostMapping("/issue/fitness")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER') or hasRole('USER')")
    public FitnessCertificate issue(@RequestParam String registrationNumber,
    @RequestParam String authority) {
    return service.issueFitness(registrationNumber, authority);
    }


    // 2️⃣ RENEW FITNESS
    @PutMapping("/renew/fitness/{registrationNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER') or hasRole('USER')")
    public FitnessCertificate renew(@PathVariable String registrationNumber) {
    return service.renewFitness(registrationNumber);
    }


    // 3️⃣ FITNESS STATUS    
    @GetMapping("/fitness/status/{registrationNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER') or hasRole('USER')")
    public ResponseEntity<ApiResponse> getFitnessStatus(
            @PathVariable String registrationNumber) {

        String status = service.getStatus(registrationNumber);

        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "Fitness status fetched",
                        Map.of(
                                "registrationNumber", registrationNumber,
                                "status", status
                        )
                )
        );
    }

    
 // 4️⃣ UPDATE FITNESS
    @PutMapping("/fitness/update/{registrationNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> updateFitness(
            @PathVariable String registrationNumber,
            @Valid @RequestBody FitnessUpdateRequest request) 
    {

        FitnessCertificate updated = service.updateFitness(
                registrationNumber,
                request.getIssuedDate(),
                request.getExpiryDate(),
                request.getIssuingAuthority(),
                request.getStatus()
        );

        return ResponseEntity.ok(
                new ApiResponse(true, "Fitness certificate updated successfully", updated)
        );
    }

    
    
 // ========== 10 PUCC Services (Pollution Under Control Certificate) ==========
 
    @Autowired
    private PUCCService puccService;

    // 1️⃣ ISSUE PUCC
    @PostMapping("/pucc/issue")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> issuePUCC(@Valid @RequestBody PUCCRequest request)
    { 

    	
    	
    	
    	
        PUCCCertificate pucc = puccService.issuePUCC(request);

        return ResponseEntity.ok(
                new ApiResponse(true, "PUCC issued successfully", pucc)
        );
        
    }
    

    // 2️⃣ FETCH PUCC
    @GetMapping("/pucc/fetch/{registrationNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER') or hasRole('USER')")
    public ResponseEntity<ApiResponse> fetchPUCC(@PathVariable String registrationNumber) 
    {

        PUCCResponse response = puccService.fetchPUCC(registrationNumber);

        return ResponseEntity.ok(
                new ApiResponse(true, "PUCC fetched successfully", response)
        );
    }

   

    // 3 PUCC EXPIRY CHECK    
    @GetMapping("/pucc/validity/{registrationNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER') or hasRole('USER')")
    public ResponseEntity<ApiResponse> checkPUCCValidity(
            @PathVariable String registrationNumber) 
    {

        boolean valid = puccService.isPUCCValid(registrationNumber);

        String message = valid
                ? "PUCC is valid (Not Expired)"
                : "PUCC has expired";

        return ResponseEntity.ok(
                new ApiResponse(true, message, valid)
        );
    }


    
 // 4️⃣ RENEW PUCC
    @PutMapping("/pucc/renew/{registrationNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER')")
    public ResponseEntity<ApiResponse> renewPUCC(
            @PathVariable String registrationNumber,
            @RequestParam String testingCenter) 
    {

        PUCCCertificate pucc = puccService.renewPUCC(registrationNumber, testingCenter);

        return ResponseEntity.ok
        (
                new ApiResponse(true, "PUCC renewed successfully", pucc)
        );
        
    }


    
    
    
    
    // ========== Utility Methods ==========
    
    private Map<String, Object> createMap(String key, Object value) 
    {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
    
    
}