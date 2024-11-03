package com.rra.ebm.EBMApplication.repository;

import com.rra.ebm.EBMApplication.domain.Requests;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RequestRepo extends JpaRepository<Requests, Integer> {
     Optional<Requests>  findByTinNumber(int tinNumber);
     List<Requests>findByStatus(String status);
}
