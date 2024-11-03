package com.rra.ebm.EBMApplication.service;

import com.rra.ebm.EBMApplication.domain.Users;
import com.rra.ebm.EBMApplication.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepo usersRepo;
    private final PasswordEncoder encoder;
    @Autowired
    public UsersService(UsersRepo usersRepo, PasswordEncoder encoder) {
        this.usersRepo = usersRepo;
        this.encoder = encoder;
    }

    public void saveUser(Users users){
        if(users!=null){
            usersRepo.save(users);
        }
    }
    public List<Users>findAllUsers(){
        return usersRepo.findAll();
    }
    public Users findUser(int tin){
        return usersRepo.findById(tin).orElse(null);
    }

    public void changePassword(int userId, String oldPassword, String newPassword) {
        Users user = usersRepo.findById(userId)
                .orElseThrow();
        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        user.setPassword(encoder.encode(newPassword));
        user.setDefault(false);
        usersRepo.save(user);
    }
}
