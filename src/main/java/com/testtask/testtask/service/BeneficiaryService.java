package com.testtask.testtask.service;

import com.testtask.testtask.model.Beneficiary;

import java.util.List;
import java.util.Optional;

public interface BeneficiaryService {

    void createBeneficiary(Beneficiary beneficiary);

    Beneficiary getBeneficiaryById(Long id);

    List<Beneficiary> getAllBeneficiaries();

    Beneficiary updateBeneficiary(Long id, Beneficiary beneficiary);

    void deleteBeneficiaryById(Long id);

    Beneficiary addBankAccountToBeneficiary(Long beneficiaryId);


}
