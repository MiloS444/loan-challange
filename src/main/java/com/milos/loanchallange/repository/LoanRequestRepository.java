package com.milos.loanchallange.repository;

import com.milos.loanchallange.model.database.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {

    boolean existsByAmountAndAnnualInterestPercentageAndNumberMonths(Long amount,
            Integer annualInterestPercentage,
            Integer numberMonths);
}
