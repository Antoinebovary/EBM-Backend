package com.rra.ebm.EBMApplication.controller;

import com.rra.ebm.EBMApplication.domain.TaxPayer;
import com.rra.ebm.EBMApplication.service.TaxPayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/taxPayer")
public class TaxPayerController {

    @Autowired
    private TaxPayerService taxPayerService;

    @PostMapping(value = "/savePayer")
    public ResponseEntity<?> saveTaxPayer(@RequestBody TaxPayer taxPayer) {
        if (taxPayer != null) {
            TaxPayer payer = taxPayerService.findTaxPayer(taxPayer.getTin());
            if (payer == null) {
                taxPayerService.saveTaxPayer(taxPayer);
                return new ResponseEntity<>("Tax Payer saved", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Tax Payer already exists", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Tax Payer is null", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/allPayers")
    public ResponseEntity<?> allTaxPayers() {
        List<TaxPayer> allPayers = taxPayerService.allTaxPayers();
        if (allPayers != null) {
            return new ResponseEntity<>(allPayers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/findPayer/{tin}")
    public ResponseEntity<?> findTaxPayer(@PathVariable("tin") int tin) {
        TaxPayer payer = taxPayerService.findTaxPayer(tin);
        if (payer != null) {
            return new ResponseEntity<>(payer, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
