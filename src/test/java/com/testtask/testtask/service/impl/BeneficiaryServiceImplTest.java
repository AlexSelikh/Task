package com.testtask.testtask.service.impl;

import com.testtask.testtask.model.BankAccount;
import com.testtask.testtask.model.Beneficiary;
import com.testtask.testtask.repository.BeneficiaryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BeneficiaryServiceImpl")
class BeneficiaryServiceImplTest {

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @InjectMocks
    private BeneficiaryServiceImpl beneficiaryService;


    @Test
    @DisplayName("Should throw an exception when the id does not exist")
    void getBeneficiaryByIdWhenIdDoesNotExistThenThrowException() {
        Long id = 1L;
        when(beneficiaryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> beneficiaryService.getBeneficiaryById(id));

        verify(beneficiaryRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should return the beneficiary when the id exists")
    void getBeneficiaryByIdWhenIdExists() {
        Long beneficiaryId = 1L;
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setId(beneficiaryId);
        beneficiary.setName("John Doe");
        beneficiary.setPinCode("1234");

        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.of(beneficiary));

        Beneficiary result = beneficiaryService.getBeneficiaryById(beneficiaryId);

        assertEquals(beneficiary, result);
        verify(beneficiaryRepository, times(1)).findById(beneficiaryId);
    }

    @Test
    @DisplayName("Should create a new beneficiary and save it to the repository")
    void createBeneficiaryAndSaveToRepository() {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setName("John Doe");
        beneficiary.setPinCode("1234");

        when(beneficiaryRepository.save(any(Beneficiary.class))).thenReturn(beneficiary);

        assertDoesNotThrow(() -> beneficiaryService.createBeneficiary(beneficiary));

        verify(beneficiaryRepository, times(1)).save(beneficiary);
    }

    @Test
    void testGetAllBeneficiaries() {
        // Arrange
        List<Beneficiary> expectedBeneficiaries = new ArrayList<>();
        expectedBeneficiaries.add(new Beneficiary(1L,"Alex","1234",new ArrayList<BankAccount>()));
        expectedBeneficiaries.add(new Beneficiary(2L,"Jon","1234",new ArrayList<BankAccount>()));
        when(beneficiaryRepository.findAll()).thenReturn(expectedBeneficiaries);

        // Act
        List<Beneficiary> actualBeneficiaries = beneficiaryService.getAllBeneficiaries();

        // Assert
        assertEquals(expectedBeneficiaries, actualBeneficiaries);
    }

    @Test
    void testGetAllBeneficiariesEmptyList() {
        // Arrange
        List<Beneficiary> expectedBeneficiaries = new ArrayList<>();
        when(beneficiaryRepository.findAll()).thenReturn(expectedBeneficiaries);

        // Act
        List<Beneficiary> actualBeneficiaries = beneficiaryService.getAllBeneficiaries();

        // Assert
        assertEquals(expectedBeneficiaries, actualBeneficiaries);
    }

    @Test
    public void testUpdateBeneficiary_PositiveCase() {
        // Arrange
        Long id = 1L;
        Beneficiary existingBeneficiary = new Beneficiary();
        existingBeneficiary.setId(id);
        existingBeneficiary.setName("John");
        existingBeneficiary.setPinCode("1234");

        Beneficiary updatedBeneficiary = new Beneficiary();
        updatedBeneficiary.setName("Updated Name");
        updatedBeneficiary.setPinCode("5678");

        when(beneficiaryRepository.findById(id)).thenReturn(java.util.Optional.of(existingBeneficiary));
        when(beneficiaryRepository.save(any(Beneficiary.class))).thenReturn(updatedBeneficiary);

        // Act
        Beneficiary result = beneficiaryService.updateBeneficiary(id, updatedBeneficiary);

        // Assert
        assertEquals(updatedBeneficiary.getName(), result.getName());
        assertEquals(updatedBeneficiary.getPinCode(), result.getPinCode());
    }

    @Test()
    public void testUpdateBeneficiary_NegativeCase() {
        // Arrange
        Long id = 1L;
        Beneficiary updatedBeneficiary = new Beneficiary();
        updatedBeneficiary.setName("Updated Name");
        updatedBeneficiary.setPinCode("5678");

        when(beneficiaryRepository.findById(id)).thenReturn(java.util.Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> beneficiaryService.updateBeneficiary(1L,updatedBeneficiary));
        // Act
        assertEquals("Не найден Бенефициар с ID  1", exception.getMessage());

    }
}
