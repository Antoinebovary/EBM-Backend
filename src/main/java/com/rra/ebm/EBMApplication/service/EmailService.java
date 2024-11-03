package com.rra.ebm.EBMApplication.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }


    public void sendingEmails(String rec,String subject,String text){
        try {
            MimeMessage message=emailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom("nyrealestate001@gmail.com");
            helper.setTo(rec);
            helper.setSubject(subject);
            helper.setText(text);
            emailSender.send(message);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("antondicrack33@gmail.com"); // Set the sender email

        mailSender.send(message);
    }
}
