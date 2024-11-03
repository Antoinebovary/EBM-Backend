package com.rra.ebm.EBMApplication.service;

import com.rra.ebm.EBMApplication.domain.Requests;
import com.rra.ebm.EBMApplication.repository.RequestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class RequestService {
    
    @Value("${upload.path}")
    private String uploadPath;

    private final RequestRepo requestRepo;
    private final EmailService emailService;

    @Autowired
    public RequestService(RequestRepo requestRepo, EmailService emailService) {
        this.requestRepo = requestRepo;
        this.emailService = emailService;
    }

    public void saveRequest(Requests requests, MultipartFile letter, MultipartFile certificate,
                            MultipartFile vatCertificate, MultipartFile idCard) throws IOException {

        // Ensure files are not null before processing
        String letterPath = (letter != null && !letter.isEmpty()) ? saveFile(letter) : null;
        String certPath = (certificate != null && !certificate.isEmpty()) ? saveFile(certificate) : null;
        String vatPath = (vatCertificate != null && !vatCertificate.isEmpty()) ? saveFile(vatCertificate) : null;
        String idPath = (idCard != null && !idCard.isEmpty()) ? saveFile(idCard) : null;

        Requests req = new Requests();

        try {
            req.setTinNumber(requests.getTinNumber());
            req.setSerialNumber(requests.getSerialNumber());
            req.setOwner(requests.getOwner());
            req.setEbmType(requests.getEbmType());
            req.setStatus(requests.getStatus());
            req.setRequestDate(LocalDate.now());
            req.setLetterPath(letterPath);
            req.setCertPath(certPath);
            req.setVatPath(vatPath);
            req.setIdPath(idPath);
            requestRepo.save(req);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be null or empty.");
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filesDirectory = Paths.get(uploadPath);

        if (!Files.exists(filesDirectory)) {
            Files.createDirectories(filesDirectory);
        }

        Path filePath = filesDirectory.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }

    public List<Requests> allRequests() {
        return requestRepo.findAll();
    }

    public Requests findRequest(int id) {
        return requestRepo.findById(id).orElse(null);
    }

    public Requests findByTin(int tin) {
        return requestRepo.findByTinNumber(tin).orElse(null);
    }

    @Transactional
    public void updateRequest(int tin, String status) {
        Requests req = requestRepo.findByTinNumber(tin).orElse(null);
        if (req != null) {
            req.setStatus(status);
            requestRepo.save(req);
        }
    }

    public boolean deleteRequest(int tin) {
        Requests req = requestRepo.findByTinNumber(tin).orElse(null);
        if (req != null) {
            requestRepo.delete(req);
            return true;
        }
        return false;
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // 24 hours
    public void sendReminder() {
        List<Requests> pendingRequests = requestRepo.findByStatus("pending");
        for (Requests req : pendingRequests) {
            String adminEmail = "dukundaneremy2001@gmail.com"; // Replace with actual admin email
            String subject = "Reminder: Pending Application";
            String text = "This is a reminder that the application with TIN number "
                          + req.getTinNumber() + " is pending approval.";
            emailService.sendingEmails(adminEmail, subject, text);
        }
    }
}
