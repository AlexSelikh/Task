package com.testtask.testtask.controller;


import com.testtask.testtask.model.BankAccount;
import com.testtask.testtask.model.Beneficiary;
import com.testtask.testtask.service.BankAccountService;
import com.testtask.testtask.service.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/accounts")
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final BeneficiaryService beneficiaryService;


    @PostMapping("/create")
    public ResponseEntity<Beneficiary> createAccount(@Valid @RequestBody Beneficiary beneficiary) {
        beneficiaryService.createBeneficiary(beneficiary);
        bankAccountService.createBankAccount(beneficiary);
        return ResponseEntity.ok(beneficiary);
    }

    @PostMapping("/addAccount/{id}")
    public ResponseEntity<BankAccount> addBankAccountToBeneficiary(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bankAccountService.createBankAccount(beneficiaryService.addBankAccountToBeneficiary(id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Beneficiary> getBeneficiaryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(beneficiaryService.getBeneficiaryById(id));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Beneficiary>> readAllBeneficiary() {
        return ResponseEntity.ok(beneficiaryService.getAllBeneficiaries());
    }

    @GetMapping("/getAllAccountsOfBeneficiary/{id}")
    public ResponseEntity<List<BankAccount>> readAllBankAccountOfBeneficiary(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bankAccountService.getBankAccountsByBeneficiary(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Beneficiary> update(@PathVariable("id") Long id,
                                              @Valid @RequestBody Beneficiary beneficiary) {
        return ResponseEntity.ok(beneficiaryService.updateBeneficiary(id, beneficiary));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBeneficiary(@PathVariable("id") Long id) {
        beneficiaryService.deleteBeneficiaryById(id);
        return ResponseEntity.ok("Account Deleted");

    }
}