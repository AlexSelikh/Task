package com.testtask.testtask.service;

import com.testtask.testtask.model.BankAccount;
import com.testtask.testtask.model.Beneficiary;

import java.util.List;
import java.util.Optional;

public interface BankAccountService {

    BankAccount createBankAccount(Beneficiary beneficiary);

    List<BankAccount> getBankAccountsByBeneficiary(Long beneficiaryId);

   /* BankAccount findByAccountNumber(String accountNumber);*/


}

