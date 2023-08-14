package com.testtask.testtask.repository;

import com.testtask.testtask.model.BankAccount;
import com.testtask.testtask.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {


    Optional<BankAccount> findByAccountNumber(String accountNumber);

    List<BankAccount> findByBeneficiary(Beneficiary beneficiary);
}