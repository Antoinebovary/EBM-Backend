package com.rra.ebm.EBMApplication.service;


import com.rra.ebm.EBMApplication.domain.TaxPayer;
import com.rra.ebm.EBMApplication.repository.TaxPayerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxPayerService {


    private final TaxPayerRepo taxPayerRepo;
    @Autowired
    public TaxPayerService(TaxPayerRepo taxPayerRepo) {
        this.taxPayerRepo = taxPayerRepo;
    }

    public void saveTaxPayer(TaxPayer taxPayer){
        if(taxPayer!=null) {
            taxPayerRepo.save(taxPayer);
        }
    }
    public List<TaxPayer> allTaxPayers(){
        return taxPayerRepo.findAll();
    }
    public TaxPayer findTaxPayer(int tin){
        return taxPayerRepo.findById(tin).orElse(null);

    }
}
