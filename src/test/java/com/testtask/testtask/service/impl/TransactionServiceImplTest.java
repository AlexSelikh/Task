package com.testtask.testtask.service.impl;

import com.testtask.testtask.model.BankAccount;
import com.testtask.testtask.model.Beneficiary;
import com.testtask.testtask.model.Transaction;
import com.testtask.testtask.repository.BankAccountRepository;
import com.testtask.testtask.repository.BeneficiaryRepository;
import com.testtask.testtask.repository.TransactionRepository;
import com.testtask.testtask.service.ValidationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TransactionServiceImpl.class})
@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionServiceImplTest")
class TransactionServiceImplTest {

    @Autowired
    private TransactionServiceImpl transactionServiceImpl;

    @Mock
    private ValidationService validationService;
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private BeneficiaryRepository beneficiaryRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    public void deposit_PositiveCase() {
        // Arrange
        Long beneficiaryId = 1L;
        String pinCode = "1234";
        String receiverAccountNumber = "1234567890";
        BigDecimal amount = new BigDecimal("100.00");

        Beneficiary beneficiary = new Beneficiary();
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(amount);

        when(validationService.validateBeneficiaryById(beneficiaryId)).thenReturn(beneficiary);
        when(validationService.validateAccountNumber(receiverAccountNumber)).thenReturn(bankAccount);

        // Act
        transactionService.deposit(beneficiaryId, pinCode, receiverAccountNumber, amount);

        // Assert
        verify(validationService, times(1)).validateBeneficiaryById(beneficiaryId);
        verify(validationService, times(1)).validatePinCode(pinCode, beneficiary);
        verify(validationService, times(1)).validateAccountNumber(receiverAccountNumber);
        verify(bankAccountRepository, times(1)).save(bankAccount);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void deposit_InvalidBeneficiary() {
        Long beneficiaryId = 1L;
        String pinCode = "1234";
        String receiverAccountNumber = "1234567890";
        BigDecimal amount = new BigDecimal("100.00");

        when(validationService.validateBeneficiaryById(beneficiaryId)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () ->
                transactionService.deposit(beneficiaryId, pinCode, receiverAccountNumber, amount));
    }

    @Test
    public void deposit_InvalidPinCode() {
        Long beneficiaryId = 1L;
        String pinCode = "1234";
        String receiverAccountNumber = "1234567890";
        BigDecimal amount = new BigDecimal("100.00");

        Beneficiary beneficiary = new Beneficiary();

        when(validationService.validateBeneficiaryById(beneficiaryId)).thenReturn(beneficiary);
        doThrow(RuntimeException.class).when(validationService).validatePinCode(pinCode, beneficiary);

        assertThrows(RuntimeException.class, () ->
                transactionService.deposit(beneficiaryId, pinCode, receiverAccountNumber, amount));

    }

    @Test
    public void deposit_InvalidAccountNumber() {
        Long beneficiaryId = 1L;
        String pinCode = "1234";
        String receiverAccountNumber = "1234567890";
        BigDecimal amount = new BigDecimal("100.00");

        Beneficiary beneficiary = new Beneficiary();

        when(validationService.validateBeneficiaryById(beneficiaryId)).thenReturn(beneficiary);
        when(validationService.validateAccountNumber(receiverAccountNumber)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () ->
                transactionService.deposit(beneficiaryId, pinCode, receiverAccountNumber, amount));
    }


}
