package com.rra.ebm.EBMApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.rra.ebm.EBMApplication.domain.Users;
import com.rra.ebm.EBMApplication.repository.UsersRepo;
import com.rra.ebm.EBMApplication.service.EmailService;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5501", "http://localhost:3000", "http://127.0.0.1:5500"}, allowedHeaders = "*")
public class ForgotPasswordController {

    @Autowired
    private UsersRepo userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

    @PostMapping("/forgot-password")
    public ResponseEntity<String> processForgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        // Check if email is provided
        if (email == null || email.isEmpty()) {
            logger.warn("Email is missing in the request.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required.");
        }

        // Fetch the users by email
        List<Users> usersList = userRepository.findByEmail(email);
        if (usersList.isEmpty()) {
            logger.warn("User with email {} not found.", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } else if (usersList.size() > 1) {
            logger.warn("Multiple users found with the email: {}", email);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Multiple users found with this email.");
        }

        Users user = usersList.get(0);

        // Generate a reset token
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        userRepository.save(user); // Save the user with the reset token

        // Prepare and send the reset password email
        String resetLink = "http://localhost:3000/reset-password/" + token;
        String subject = "Reset Password";
        String body = "Hello from EBM!\n\nWe received a request to reset your password for your EBM account. To reset it, simply click the link below:\n"
                + resetLink
                + "\n\nIf you didn't request a password reset, please ignore this message.\n\nThank you for working with us!\n\nBest regards,\nThe EBM Team";

        try {
            emailService.sendSimpleEmail(user.getEmail(), subject, body);
            logger.info("Reset password email sent to {}", email);
            return ResponseEntity.ok("Reset password email sent.");
        } catch (Exception e) {
            logger.error("Failed to send reset email to {}: {}", user.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send reset email.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        // Validate input
        if (token == null || token.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            logger.warn("Token or new password is missing in the request.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token and new password are required.");
        }

        // Find users by reset token
        List<Users> usersList = userRepository.findByResetPasswordToken(token);
        if (usersList.isEmpty()) {
            logger.warn("Invalid or expired reset token: {}", token);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid or expired reset token.");
        } else if (usersList.size() > 1) {
            logger.warn("Multiple users found with the same reset token: {}", token);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Multiple users found with this reset token.");
        }

        Users user = usersList.get(0);

        try {
            // Update password
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetPasswordToken(null); // Clear the reset token
            userRepository.save(user);

            logger.info("Password successfully reset for user: {}", user.getEmail());

            // Send confirmation email
            String subject = "Password Reset Successful";
            String body = "Hello from EBM!\n\nYour password has been successfully reset. If you did not perform this action, please contact our support team immediately.\n\nBest regards,\nThe VubaRide Team";
            
            emailService.sendSimpleEmail(user.getEmail(), subject, body);

            return ResponseEntity.ok("Password successfully reset.");
            
        } catch (Exception e) {
            logger.error("Error resetting password: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while resetting the password.");
        }
    }
}
