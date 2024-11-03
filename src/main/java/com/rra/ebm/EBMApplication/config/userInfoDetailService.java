package com.rra.ebm.EBMApplication.config;

import com.rra.ebm.EBMApplication.domain.Users;
import com.rra.ebm.EBMApplication.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class userInfoDetailService implements UserDetailsService {

   @Autowired
    private UsersRepo usersRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        int tinNumber;
        try {
            tinNumber = Integer.parseInt(username);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Invalid username: " + username);
        }

        Optional<Users> user = usersRepo.findById(tinNumber);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found with TIN number: " + tinNumber));
        return user.map(UserInfoDetails::new).get();
    }
}
