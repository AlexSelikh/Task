package com.testtask.testtask.service.impl;

import com.testtask.testtask.model.BankAccount;
import com.testtask.testtask.model.Beneficiary;
import com.testtask.testtask.repository.BankAccountRepository;
import com.testtask.testtask.repository.BeneficiaryRepository;
import com.testtask.testtask.service.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    @Override
    @Transactional
    public BankAccount createBankAccount(Beneficiary beneficiary) {
        BankAccount account = new BankAccount();
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(BigDecimal.ZERO);
        account.setBeneficiary(beneficiary);
        account.setPinCode(beneficiary.getPinCode());
        return bankAccountRepository.save(account);

    }

    @Override
    public List<BankAccount> getBankAccountsByBeneficiary(Long beneficiaryId) {
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new RuntimeException("Beneficiary with ID " + beneficiaryId + " does not exist"));
        return new ArrayList<>(beneficiary.getAccounts());
    }

  /*  @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        return bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account Number  " + accountNumber + " does not exist"));
    }*/

    private String generateAccountNumber() {
        return UUID.randomUUID().toString();
    }

}


