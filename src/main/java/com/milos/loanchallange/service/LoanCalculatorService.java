package com.milos.loanchallange.service;

import com.milos.loanchallange.model.LoanRequest;

public interface LoanCalculatorService {

    void getInstallmentPlan(LoanRequest loanRequest);
}
