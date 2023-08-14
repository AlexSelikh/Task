package com.testtask.testtask.service.impl;


import com.testtask.testtask.model.BankAccount;
import com.testtask.testtask.model.Beneficiary;
import com.testtask.testtask.repository.BankAccountRepository;
import com.testtask.testtask.repository.BeneficiaryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("BankAccountServiceImplTest")
 class BankAccountServiceImplTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    @Test
    public void testCreateBankAccount_Positive() {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setPinCode("1234");

        BankAccount dummyAccount = new BankAccount();
        dummyAccount.setId(1L);
        dummyAccount.setAccountNumber("123456789");
        dummyAccount.setBalance(BigDecimal.ZERO);
        dummyAccount.setBeneficiary(beneficiary);
        dummyAccount.setPinCode("1234");

        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(dummyAccount);

        BankAccount createdAccount = bankAccountService.createBankAccount(beneficiary);

        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));

        assertNotNull(createdAccount);
        assertEquals("1234", createdAccount.getPinCode());
    }

    @Test
    public void testCreateBankAccount_Negative() {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setPinCode("1234");

        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(null);

        BankAccount createdAccount = bankAccountService.createBankAccount(beneficiary);

        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));

        assertNull(createdAccount);
    }

    @Test
    public void testGetBankAccountsByBeneficiary_Positive() {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setId(1L);

        BankAccount account1 = new BankAccount();
        BankAccount account2 = new BankAccount();
        beneficiary.getAccounts().add(account1);
        beneficiary.getAccounts().add(account2);

        when(beneficiaryRepository.findById(1L)).thenReturn(Optional.of(beneficiary));

        List<BankAccount> result = bankAccountService.getBankAccountsByBeneficiary(1L);
        assertEquals(2, result.size());
        assertTrue(result.contains(account1));
        assertTrue(result.contains(account2));
    }
    @Test()
    public void testGetBankAccountsByBeneficiary_Negative() {
        when(beneficiaryRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bankAccountService.getBankAccountsByBeneficiary(1L));

        assertEquals("Beneficiary with ID 1 does not exist", exception.getMessage());
    }
}
