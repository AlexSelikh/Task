package com.testtask.testtask.service;

import com.testtask.testtask.model.Transaction;

import java.math.BigDecimal;
import java.util.List;


public interface TransactionService {

    void deposit(Long beneficiaryId, String pinCode, String receiverAccountNumber, BigDecimal amount);


    void withdraw(Long beneficiaryId, String pinCode, String withdrawAccountNumber, BigDecimal amount);

    void transfer(Long beneficiaryId, String pinCode, String senderAccountNumber, String receiverAccountNumber, BigDecimal amount);

    List<Transaction> getTransactionHistory();
}
