package com.milos.loanchallange.service;

import com.milos.loanchallange.model.LoanRequestDto;

public interface LoanCalculatorService {

    void getInstallmentPlan(LoanRequestDto loanRequestDto);
}
