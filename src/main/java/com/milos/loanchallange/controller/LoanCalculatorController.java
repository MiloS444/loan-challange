package com.milos.loanchallange.controller;

import com.milos.loanchallange.model.LoanRequest;
import com.milos.loanchallange.service.LoanCalculatorService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public void getInstallmentPlan(@RequestBody final LoanRequest loanRequest) {

        loanCalculatorService.getInstallmentPlan(loanRequest);
    }
}
