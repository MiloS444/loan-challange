package com.milos.loanchallange.service;

import com.milos.loanchallange.model.LoanRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanCalculatorServiceImpl implements LoanCalculatorService {

    @Override
    public void getInstallmentPlan(final LoanRequest loanRequest) {

        if (requestExists(loanRequest)) {
        }


    }

    private boolean requestExists(final LoanRequest loanRequest) {
        return true;
    }
}
