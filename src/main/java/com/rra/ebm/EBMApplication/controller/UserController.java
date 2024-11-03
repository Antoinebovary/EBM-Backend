package com.rra.ebm.EBMApplication.controller;

import com.rra.ebm.EBMApplication.domain.AuthRequest;
import com.rra.ebm.EBMApplication.domain.PasswordRequest;
import com.rra.ebm.EBMApplication.domain.Users;
import com.rra.ebm.EBMApplication.service.EmailService;
import com.rra.ebm.EBMApplication.service.JwtService;
import com.rra.ebm.EBMApplication.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UsersService usersService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;
    @Autowired
    public UserController(UsersService usersService, EmailService emailService, AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder encoder) {
        this.usersService = usersService;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.encoder = encoder;
    }
    private String generateDefaultPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 16; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }

    @PostMapping("/register")
    public ResponseEntity<?>SignUp(@RequestBody Users users){
        if(users!=null){
            Users usr=usersService.findUser(users.getTin());
            if(usr==null) {
                String defaultPassword = generateDefaultPassword();
                users.setPassword(encoder.encode(defaultPassword));
                users.setDefault(true);
                users.setChangedAt(new Date());
                String subject = "Account Created";
                String text = "Thank you for Signing up, your password is " + defaultPassword;

                String userEmail = users.getEmail();
                if (userEmail != null) {
                    emailService.sendingEmails(userEmail, subject, text);
                    usersService.saveUser(users);
                    return new ResponseEntity<>("User saved and email sent", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("User email is null", HttpStatus.BAD_GATEWAY);
                }
            }else {
                return new ResponseEntity<>("User already Exist",HttpStatus.CONFLICT);

            }

        }
        return new ResponseEntity<>("User not Exist", HttpStatus.BAD_REQUEST);

    }
    @PostMapping(value = "/authentication", consumes = "application/json")
    public ResponseEntity<?> authReqAndToken(@RequestBody Users users){
        Users user=usersService.findUser(users.getTin());
        if(user!=null) {
            String roles=user.getRoles();
            boolean isDefault=user.isDefault();
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(String.valueOf(users.getTin()),users.getPassword()));
            if (authentication.isAuthenticated()) {

                String token = jwtService.generateToken(String.valueOf(users.getTin()),roles,isDefault);
                Map<String, String> response = new HashMap<>();
                response.put("token", token);

                return new ResponseEntity<>(response, HttpStatus.OK);

            } else {
                throw new UsernameNotFoundException("no user found");
            }
        }
        else{
            return  new ResponseEntity<>("user is null",HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping(value = "/changePassword")
    @PreAuthorize("hasAuthority('taxpayer')")
    public ResponseEntity<?>updatePassword(@RequestBody PasswordRequest request){
        try{
            usersService.changePassword(request.getUserId(),request.getOldPassword(),request.getNewPassword());
            return  ResponseEntity.ok("password changed Successfully");
        }
        catch (HttpClientErrorException.NotFound e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }



}
