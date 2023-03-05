package com.milos.loanchallange.repository;

import com.milos.loanchallange.model.database.LoanRequest;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {
    Optional<LoanRequest> findOneByAmountAndAnnualInterestPercentageAndNumberOfPayments(Long amount,
            Integer annualInterestPercentage,
            Integer numberOfPayments);
}
