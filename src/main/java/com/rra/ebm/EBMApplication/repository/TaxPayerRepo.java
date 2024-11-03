package com.rra.ebm.EBMApplication.repository;

import com.rra.ebm.EBMApplication.domain.TaxPayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxPayerRepo extends JpaRepository<TaxPayer,Integer> {
}
