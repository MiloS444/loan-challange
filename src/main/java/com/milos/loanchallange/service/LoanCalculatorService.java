package com.milos.loanchallange.service;

import com.milos.loanchallange.model.LoanRequestDto;
import java.util.List;

public interface LoanCalculatorService {

    List<String> getInstallmentPlan(LoanRequestDto loanRequestDto);
}
