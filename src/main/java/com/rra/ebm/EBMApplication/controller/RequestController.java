package com.rra.ebm.EBMApplication.controller;

import com.rra.ebm.EBMApplication.domain.Requests;
import com.rra.ebm.EBMApplication.domain.Users;
import com.rra.ebm.EBMApplication.service.EmailService;
import com.rra.ebm.EBMApplication.service.RequestService;
import com.rra.ebm.EBMApplication.service.UsersService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/applications")
// Allow CORS for the specified origin
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class RequestController {

    private final RequestService requestService;
    private final UsersService usersService;
    private final EmailService emailService;

    @Autowired
    public RequestController(RequestService requestService, UsersService usersService, EmailService emailService) {
        this.requestService = requestService;
        this.usersService = usersService;
        this.emailService = emailService;
    }

    @PostMapping(value = "/saveApplication", consumes = "multipart/form-data")
    public ResponseEntity<?> saveRequest(
            @ModelAttribute Requests requests,
            @RequestParam(name = "letter", required = false) MultipartFile letter,
            @RequestParam(name = "certificate", required = false) MultipartFile certificate,
            @RequestParam(name = "vatCertificate", required = false) MultipartFile vatCertificate,
            @RequestParam(name = "idCard", required = false) MultipartFile idCard) {

        try {
            // Check for user existence using TIN number
            int tin = requests.getTinNumber();
            Users user = usersService.findUser(tin);
            if (user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            // Safely process files if present
            if (letter != null && !letter.isEmpty()) {
                System.out.println("Received letter: " + letter.getOriginalFilename());
            }
            if (certificate != null && !certificate.isEmpty()) {
                System.out.println("Received certificate: " + certificate.getOriginalFilename());
            }
            if (vatCertificate != null && !vatCertificate.isEmpty()) {
                System.out.println("Received VAT certificate: " + vatCertificate.getOriginalFilename());
            }
            if (idCard != null && !idCard.isEmpty()) {
                System.out.println("Received ID card: " + idCard.getOriginalFilename());
            }

            // Save the request and files if they are present
            requestService.saveRequest(requests, letter, certificate, vatCertificate, idCard);

            // Send a confirmation email to the user
            String subject = "Application Received";
            String text = "Thank you for applying to get EBM Software. Your application has been received. " +
                          "We will review it and get back to you.";
            emailService.sendingEmails(user.getEmail(), subject, text);

            // Return a success response
            return new ResponseEntity<>("Application saved", HttpStatus.OK);

        } catch (Exception ex) {
            // Log the error and return a 400 status with the error message
            ex.printStackTrace();
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/allApplications")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> allRequests() {
        try {
            List<Requests> allRequests = requestService.allRequests();
            return new ResponseEntity<>(allRequests, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/findApplication/{tin}")
    @PreAuthorize("hasAnyAuthority('admin', 'taxpayer')")
    public ResponseEntity<?> findRequest(@PathVariable("tin") int tin) {
        try {
            Requests req = requestService.findByTin(tin);

            if (req != null) {
                // Generate URLs for uploaded files
                req.setLetterPath("http://localhost:8080/files/" + req.getLetterPath());
                req.setCertPath("http://localhost:8080/files/" + req.getCertPath());
                req.setVatPath("http://localhost:8080/files/" + req.getVatPath());
                req.setIdPath("http://localhost:8080/files/" + req.getIdPath());

                return new ResponseEntity<>(req, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No requests found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/approve/{tin}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> confirmRequest(@PathVariable int tin, @RequestBody Map<String, String> requestBody) {
        Users user = usersService.findUser(tin);
        String feedback = requestBody.get("feedback");

        if (user != null) {
            Requests req = requestService.findByTin(tin);
            String email = user.getEmail();
            String subject = "Application Status";

            if (req != null) {
                req.setStatus("approved");
                requestService.updateRequest(tin, "approved");
                emailService.sendingEmails(email, subject, feedback);
                return new ResponseEntity<>("Application approved", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/reject/{tin}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> rejectRequest(@PathVariable int tin, @RequestBody Map<String, String> requestBody) {
        Users user = usersService.findUser(tin);
        String feedback = requestBody.get("feedback");

        if (user != null) {
            Requests req = requestService.findByTin(tin);
            String email = user.getEmail();
            String subject = "Application Status";

            if (req != null) {
                req.setStatus("rejected");
                requestService.updateRequest(tin, "rejected");
                emailService.sendingEmails(email, subject, feedback);
                return new ResponseEntity<>("Application rejected", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/delete/{tin}")
    public ResponseEntity<?> deleteRequest(@PathVariable int tin) {
        Requests requests = requestService.findByTin(tin);

        if (requests != null) {
            requestService.deleteRequest(tin);
            return new ResponseEntity<>("Request deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Request not found", HttpStatus.NOT_FOUND);
        }
    }
}
