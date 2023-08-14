package com.testtask.testtask.service;

import com.testtask.testtask.model.BankAccount;
import com.testtask.testtask.model.Beneficiary;

import java.math.BigDecimal;

public interface ValidationService {

    Beneficiary validateBeneficiaryById(Long beneficiaryId);

    void validatePinCode(String enteredPin, Beneficiary beneficiary);

    BankAccount validatefindBankAccount(Beneficiary beneficiary, Long bankAccountId);

    void validateNewBalance(BigDecimal bigDecimal);
     BankAccount validateAccountNumber (String accountNumber);
}
