package com.testtask.testtask.service.impl;

import com.testtask.testtask.model.BankAccount;
import com.testtask.testtask.model.Beneficiary;
import com.testtask.testtask.model.Transaction;
import com.testtask.testtask.repository.BankAccountRepository;
import com.testtask.testtask.repository.TransactionRepository;
import com.testtask.testtask.service.TransactionOperation;
import com.testtask.testtask.service.TransactionService;
import com.testtask.testtask.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final ValidationService validationService;

    @Override
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionRepository.findAll());
    }

    @Override
    @Transactional
    public void deposit(Long beneficiaryId, String pinCode, String receiverAccountNumber, BigDecimal amount) {

        Beneficiary beneficiaryDeposit = validationService.validateBeneficiaryById(beneficiaryId);
        validationService.validatePinCode(pinCode, beneficiaryDeposit);
        BankAccount bankAccountReceiver = validationService.validateAccountNumber(receiverAccountNumber);

        updateAccountAndSaveTransaction(bankAccountReceiver, amount, TransactionOperation.ADD);
    }

    @Override
    @Transactional
    public void withdraw(Long beneficiaryId, String pinCode, String withdrawAccountNumber, BigDecimal amount) {

        Beneficiary beneficiaryWithdraw = validationService.validateBeneficiaryById(beneficiaryId);
        validationService.validatePinCode(pinCode, beneficiaryWithdraw);

        BankAccount withdrawbankAccount = validationService.validateAccountNumber(withdrawAccountNumber);
        updateAccountAndSaveTransaction(withdrawbankAccount, amount, TransactionOperation.SUBTRACT);
    }

   private void updateAccountAndSaveTransaction(BankAccount bankAccount, BigDecimal amount, TransactionOperation operation) {
        BigDecimal currentBalance = bankAccount.getBalance();
        BigDecimal newBalance;

        if (operation == TransactionOperation.ADD) {
            newBalance = currentBalance.add(amount);
        } else if (operation == TransactionOperation.SUBTRACT) {
            validationService.validateNewBalance(newBalance = currentBalance.subtract(amount));
        } else {
            throw new IllegalArgumentException("Unsupported operation");
        }

        bankAccount.setBalance(newBalance);
        bankAccountRepository.save(bankAccount);

        Transaction transaction = createTransaction("", bankAccount.getAccountNumber(), amount, operation);
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void transfer(Long beneficiaryId, String pinCode, String senderAccountNumber, String receiverAccountNumber, BigDecimal amount) {
        Beneficiary beneficiaryTransferSender = validationService.validateBeneficiaryById(beneficiaryId);
        validationService.validatePinCode(pinCode, beneficiaryTransferSender);

        BankAccount bankAccountSender = validationService.validateAccountNumber(senderAccountNumber);
        BankAccount bankAccountReceiver = validationService.validateAccountNumber(receiverAccountNumber);

        updateAccountAndSaveTransaction(bankAccountSender, bankAccountReceiver, amount);
    }

    private void updateAccountAndSaveTransaction(BankAccount senderAccount, BankAccount receiverAccount, BigDecimal amount) {
        BigDecimal currentBalanceSender = senderAccount.getBalance();
        BigDecimal currentBalanceReceiver = receiverAccount.getBalance();

        BigDecimal newBalanceSender = currentBalanceSender.subtract(amount);
        validationService.validateNewBalance(newBalanceSender);
        senderAccount.setBalance(newBalanceSender);
        bankAccountRepository.save(senderAccount);

        BigDecimal newBalanceReceiver = currentBalanceReceiver.add(amount);
        receiverAccount.setBalance(newBalanceReceiver);
        bankAccountRepository.save(receiverAccount);

        Transaction transactionSender = createTransaction(senderAccount.getAccountNumber(), receiverAccount.getAccountNumber(), amount, TransactionOperation.TRANSFER);
        Transaction transactionReceiver = createTransaction(senderAccount.getAccountNumber(), receiverAccount.getAccountNumber(), amount, TransactionOperation.TRANSFER);
        transactionRepository.save(transactionSender);
        transactionRepository.save(transactionReceiver);
    }

    private Transaction createTransaction(String fromAccount, String toAccount, BigDecimal amount, TransactionOperation operation) {
        Transaction transaction = new Transaction();

        if (operation == TransactionOperation.ADD) {
            transaction.setFromAccount("Deposit");
        } else if (operation == TransactionOperation.SUBTRACT) {
            transaction.setFromAccount("Withdraw");
        } else if (operation == TransactionOperation.TRANSFER) {
            transaction.setFromAccount(fromAccount);
        } else {
            throw new IllegalArgumentException("Unsupported operation");
        }

        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setPinCode("****");
        transaction.setTimestamp(new Timestamp(System.currentTimeMillis()));

        return transaction;
    }
}
