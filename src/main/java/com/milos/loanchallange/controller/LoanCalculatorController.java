package com.milos.loanchallange.controller;

import com.milos.loanchallange.model.LoanRequestDto;
import com.milos.loanchallange.service.LoanCalculatorService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/loans")
@AllArgsConstructor
public class LoanCalculatorController {

    private final LoanCalculatorService loanCalculatorService;

    @PostMapping
    public List<String> getInstallmentPlan(@RequestBody final LoanRequestDto loanRequestDto) {

        return loanCalculatorService.getInstallmentPlan(loanRequestDto);
    }
}
