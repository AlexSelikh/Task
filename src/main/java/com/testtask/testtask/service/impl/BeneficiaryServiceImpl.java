package com.testtask.testtask.service.impl;

import com.testtask.testtask.model.Beneficiary;
import com.testtask.testtask.repository.BeneficiaryRepository;
import com.testtask.testtask.service.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;

    @Override
    @Transactional
    public void createBeneficiary(Beneficiary beneficiary) {
        beneficiary.setName(beneficiary.getName());
        beneficiary.setPinCode(beneficiary.getPinCode());
        beneficiaryRepository.save(beneficiary);
    }

    @Override
    public Beneficiary getBeneficiaryById(Long id) {
        return beneficiaryRepository.findById(id).orElseThrow(() -> exceptionReturner(id));
    }
    @Override
    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryRepository.findAll();
    }

    @Override
    @Transactional
    public Beneficiary updateBeneficiary(Long id, Beneficiary updatedBeneficiary) {

        Beneficiary existingBeneficiary = beneficiaryRepository.findById(id).orElseThrow(() -> exceptionReturner(id));

        existingBeneficiary.setName(updatedBeneficiary.getName());
        existingBeneficiary.setPinCode(updatedBeneficiary.getPinCode());

        return beneficiaryRepository.save(existingBeneficiary);

    }

    @Override
    @Transactional
    public void deleteBeneficiaryById(Long beneficiaryId) {
        beneficiaryRepository.findById(beneficiaryId).orElseThrow(() -> exceptionReturner(beneficiaryId));
        beneficiaryRepository.deleteById(beneficiaryId);
    }

    @Override
    public Beneficiary addBankAccountToBeneficiary(Long beneficiaryId) {
        return beneficiaryRepository.findById(beneficiaryId).orElseThrow(() -> exceptionReturner(beneficiaryId));
    }

    private EntityNotFoundException exceptionReturner(Long id) {
        return new EntityNotFoundException("Не найден Бенефициар с ID  " + id);
    }
}