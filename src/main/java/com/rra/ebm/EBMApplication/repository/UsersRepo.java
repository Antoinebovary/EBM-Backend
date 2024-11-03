package com.rra.ebm.EBMApplication.repository;

import com.rra.ebm.EBMApplication.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsersRepo extends JpaRepository<Users, Integer> {
    List<Users> findByResetPasswordToken(String resetPasswordToken);
    List<Users> findByEmail(String email);
}
