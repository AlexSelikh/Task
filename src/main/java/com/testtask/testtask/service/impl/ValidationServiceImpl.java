package com.testtask.testtask.service.impl;

import com.testtask.testtask.model.BankAccount;
import com.testtask.testtask.model.Beneficiary;
import com.testtask.testtask.repository.BankAccountRepository;
import com.testtask.testtask.repository.BeneficiaryRepository;
import com.testtask.testtask.service.ValidationService;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {
    private final BeneficiaryRepository beneficiaryRepository;
    private final BankAccountRepository bankAccountRepository;

    @Override
    public Beneficiary validateBeneficiaryById(Long beneficiaryId) {
        return beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> exceptionReturner(beneficiaryId));
    }

    @Override
    public void validatePinCode(String enteredPin, Beneficiary beneficiary) {
        if (!beneficiary.getPinCode().equals(enteredPin)) {
            throw new RuntimeException("Invalid pin code");
        }
    }

    @Override
    public BankAccount validatefindBankAccount(Beneficiary beneficiary, Long bankAccountId) {
        return beneficiary.getAccounts().stream()
                .filter(account -> account.getId().equals(bankAccountId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Account with ID " + bankAccountId + " does not exist"));
    }

    @Override
    public void validateNewBalance(BigDecimal bigDecimal) {
        if (bigDecimal.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
    }

    @Override
    public BankAccount validateAccountNumber(String accountNumber) {
       return bankAccountRepository.findByAccountNumber(accountNumber)
               .orElseThrow(() -> new RuntimeException("Account Number  " + accountNumber + " does not exist"));

    }

    private EntityNotFoundException exceptionReturner(Long id) {
        return new EntityNotFoundException("Не найден Бенефициар с ID  " + id);
    }
}
