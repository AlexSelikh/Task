package com.testtask.testtask.repository;

import com.testtask.testtask.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {



}